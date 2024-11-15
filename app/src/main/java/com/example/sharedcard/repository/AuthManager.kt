package com.example.sharedcard.repository

import android.util.Log
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.check.CheckDao
import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.target.TargetDao
import com.example.sharedcard.database.entity.user.UserAccountEntity
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.dto.AccountDeleteResponse
import com.example.sharedcard.service.dto.AccountResponse
import com.example.sharedcard.service.dto.RegistrationBody
import com.example.sharedcard.ui.startup.data.AuthResult
import com.example.sharedcard.ui.startup.data.RegisterResult
import com.example.sharedcard.ui.startup.data.SyncResult
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val groupManager: GroupManager,
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val groupUsersDao: GroupUsersDao,
    private val checkDao: CheckDao,
    private val targetDao: TargetDao,
    private val dictionaryRepository: DictionaryRepository,
    private val queryPreferences: QueryPreferences,
    private val authApi: AuthApi
) {


    fun authentication(
        email: String,
        password: String,
        isInternetConnection: Boolean
    ): AuthResult {
        val user = userDao.findUserAccount(email)
        if (user == null || !queryPreferences.isSync) {
            return if (isInternetConnection) {
                try {
                    val response = authApi.authorization(email, password).execute()
                    if (response.code() != 200) {
                        AuthResult(message = R.string.invalid_sign_in)
                    } else {
                        val auth = response.body()!!
                        userDao.createUserAccount(UserAccountEntity(auth.idUser, email, password))
                        saveId(auth.idUser, auth.idGroup, false)
                        AuthResult()
                    }
                } catch (e: Exception) {
                    Log.e("TAG","-------------------------------------------------",e)
                    println("------------------------------------------------------------")
                    AuthResult(message = R.string.invalid_exception, error = e)
                }
            } else {
                AuthResult(message = R.string.invalid_internet_connection)
            }
        } else {
            if (user.password != password)
                return AuthResult(message = R.string.invalid_sign_in)
            val idGroup = groupDao.findLocalGroup(user.id).idGroup
            saveId(user.id, idGroup, true)
            return AuthResult()
        }
    }

    private fun saveId(_userId: UUID, _groupId: UUID, _isSync: Boolean) {
        queryPreferences.run {
            userId = _userId
            groupId = _groupId
            isLocal = true
            isSync = _isSync

        }
    }

    fun registration(
        email: String,
        password: String,
        name: String,
        date: Date,
        gender: Boolean,
        weight: Double,
        height: Int
    ): RegisterResult {
        return try {
            val body = RegistrationBody(email,password,name,date.time,gender,weight, height)
            val response = authApi.registration(body).execute()
            if (response.code() == 200) {
                RegisterResult(codeSend = true)
            } else {
                RegisterResult(
                    message = R.string.invalid_exception,
                    error = Exception(response.errorBody()?.string() ?: "")
                )
            }
        } catch (e: Exception) {
            RegisterResult(message = R.string.invalid_exception, error = e)
        }
    }

    fun verification(email: String, password: String, code: String): RegisterResult {
        return try {
            val response = authApi.verification(email, password, code).execute()
            if (response.code() == 200) {
                val authResponse = response.body()!!
                userDao.createUserAccount(UserAccountEntity(authResponse.idUser, email, password))
                saveId(authResponse.idUser, authResponse.idGroup, false)
                RegisterResult(isContinue = true)
            } else {
                RegisterResult(
                    message = R.string.invalid_code,
                    error = Exception(response.errorBody()?.string() ?: "")
                )
            }
        } catch (e: Exception) {
            RegisterResult(error = e, message = R.string.invalid_exception)
        }
    }

    suspend fun synchronization(): SyncResult = try {
        if (queryPreferences.isSync) {
            SyncResult(isSync = true)
        } else {
            val userId = queryPreferences.userId
            val users = userDao.getAll()
            val user = userDao.getAccount(userId)
            val header = mapOf(
                AuthApi.HEADER_ID_USER to user.id.toString(),
                AuthApi.HEADER_PASSWORD_USER to user.password
            )
            val responseDictionary =
                authApi.getDictionary(header).execute()
            val body = responseDictionary.body()!!
            dictionaryRepository.synchronization(body)

            val responseAccount = authApi.getAccount(header).execute()
            val bodyAccount =  responseAccount.body()!!
            saveAccountResponse(bodyAccount,true)

            queryPreferences.isSync = true
            SyncResult(isSync = true)
        }
    } catch (e: Exception) {
        SyncResult(isSync = false, error = e)
    }

    fun saveAccountResponse(response: AccountResponse,isStartApp: Boolean) {
        var flag = true
        response.groups.forEach {
            if(it.id == queryPreferences.groupId)
                flag = false
            groupDao.insertOrUpdate(it)
        }
        if(flag && isStartApp)
            groupManager.setGroupToLocal()
        response.users.forEach {
            userDao.insertOrUpdate(it)
        }
        response.groupUsers.forEach {
            groupUsersDao.insertOrUpdate(it)
        }
        response.targets.forEach {
            targetDao.insertOrUpdate(it)
        }
        response.checks.forEach {
            checkDao.insertOrUpdate(it)
        }
    }

    fun deleteAccountResponse(accountDeleteResponse: AccountDeleteResponse) {
        accountDeleteResponse.run {
            checks.forEach{
                checkDao.delete(it)
            }
            users.forEach{
                userDao.delete(it)
            }
            groups.forEach {
                if(it ==queryPreferences.groupId)
                    groupManager.setGroupToLocal()
                groupDao.delete(it)
            }
            groupUsers.forEach {
                groupUsersDao.deleteUser(it.first,it.second)
            }
            targets.forEach {
                targetDao.delete(it)
            }
        }
    }

    fun dropAndSaveAccountResponse(accountResponse: AccountResponse) {
        groupDao.deleteAllGroups()
        saveAccountResponse(accountResponse,true)
    }
}