package com.example.sharedcard.repository

import android.util.Log
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.basket.BasketDao
import com.example.sharedcard.database.entity.purchase.PurchaseDao
import com.example.sharedcard.database.entity.gpoup_person.GroupPersonsDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.history.HistoryDao
import com.example.sharedcard.database.entity.person.PersonAccountEntity
import com.example.sharedcard.database.entity.person.PersonDao
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
    private val basketDao: BasketDao,
    private val historyDao: HistoryDao,
    private val groupDao: GroupDao,
    private val personDao: PersonDao,
    private val groupPersonsDao: GroupPersonsDao,
    private val purchaseDao: PurchaseDao,
    private val dictionaryRepository: DictionaryRepository,
    private val queryPreferences: QueryPreferences,
    private val authApi: AuthApi
) {


    fun authentication(
        email: String,
        password: String,
        isInternetConnection: Boolean
    ): AuthResult {
        val person = personDao.findPersonAccount(email)
        if (person == null || !queryPreferences.isSync) {
            return if (isInternetConnection) {
                try {
                    val response = authApi.authorization(email, password).execute()
                    if (response.code() != 200) {
                        AuthResult(message = R.string.invalid_sign_in)
                    } else {
                        val auth = response.body()!!
                        personDao.createPersonAccount(PersonAccountEntity(auth.personId, email, password))
                        saveId(auth.personId, auth.groupId, false)
                        AuthResult()
                    }
                } catch (e: Exception) {
                    Log.e("TAG","-------------------------------------------------",e)
                    AuthResult(message = R.string.invalid_exception, error = e)
                }
            } else {
                AuthResult(message = R.string.invalid_internet_connection)
            }
        } else {
            if (person.password != password)
                return AuthResult(message = R.string.invalid_sign_in)
            val groupId = groupDao.findLocalGroup(person.id).idGroup
            saveId(person.id, groupId, true)
            return AuthResult()
        }
    }

    private fun saveId(_personId: UUID, _groupId: UUID, _isSync: Boolean) {
        queryPreferences.run {
            personId = _personId
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
    ): RegisterResult {
        return try {
            val body = RegistrationBody(email,password,name,date.time,gender)
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
                personDao.createPersonAccount(PersonAccountEntity(authResponse.personId, email, password))
                saveId(authResponse.personId, authResponse.groupId, false)
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

     fun synchronization(): SyncResult = try {
        if (queryPreferences.isSync) {
            SyncResult(isSync = true)
        } else {
            val personId = queryPreferences.personId
            val person = personDao.getAccount(personId)
            val header = mapOf(
                AuthApi.HEADER_ID_PERSON to person.id.toString(),
                AuthApi.HEADER_PASSWORD_PERSON to person.password
            )
            val responseDictionary =
                authApi.getDictionary(header).execute()
            val body = responseDictionary.body()!!
            dictionaryRepository.synchronization(body)

            val responseAccount = authApi.getAccount(header).execute()
            val bodyAccount =  responseAccount.body()!!
            saveAccountResponse(bodyAccount,true)

            queryPreferences.isSync = true
            queryPreferences.currency = 1
            SyncResult(isSync = true)
        }
    } catch (e: Exception) {
        SyncResult(isSync = false, error = e)
    }

    fun saveAccountResponse(response: AccountResponse,isStartApp: Boolean) {

        response.groups.forEach {
            groupDao.insertOrUpdate(it)
        }
        response.persons.forEach {
            personDao.insertOrUpdate(it)
        }
        response.groupPersons.forEach {
            groupPersonsDao.insertOrUpdate(it)
        }
        response.purchases.forEach {
            purchaseDao.insertOrUpdate(it)
        }
        response.baskets.forEach {
            basketDao.insertOrUpdate(it)
        }
        response.histories.forEach {
            historyDao.insertOrUpdate(it)
        }
    }

    fun deleteAccountResponse(accountDeleteResponse: AccountDeleteResponse) {
        accountDeleteResponse.run {
            purchases.forEach{
                purchaseDao.delete(it)
            }
            persons.forEach{
                personDao.delete(it)
            }
            groups.forEach {
                groupDao.delete(it)
            }
            groupPersons.forEach {
                groupPersonsDao.deletePerson(it.first,it.second)
            }
            baskets.forEach {
                basketDao.delete(it)
            }
        }
    }

    fun dropAndSaveAccountResponse(accountResponse: AccountResponse) {
        groupDao.deleteAllGroups()
        saveAccountResponse(accountResponse,true)
    }
}