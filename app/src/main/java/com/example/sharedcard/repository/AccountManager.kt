package com.example.sharedcard.repository

import android.graphics.Bitmap
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.api.FileApi
import com.example.sharedcard.service.dto.FileResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.ui.profile.data.ImageResult
import com.example.sharedcard.ui.profile.data.UserImage
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    private val userDao: UserDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper,
    private val fileApi: FileApi
) {


    fun getUser() = userDao.getLiveData(queryPreferences.userId)
    fun getUserAccountLiveData() = userDao.getAccountLiveData(queryPreferences.userId)
    fun getUserAccount() = userDao.getAccount(queryPreferences.userId)
    fun accountExists() =
        queryPreferences.userId != UUID.fromString(QueryPreferences.DEF_VALUE) && queryPreferences.isSync


    fun exitFromAccount() {
        queryPreferences.run {
            groupId = UUID.fromString(QueryPreferences.DEF_VALUE)
            userId = UUID.fromString(QueryPreferences.DEF_VALUE)
        }
    }

    fun setImage(isInternetConnection: Boolean, bitmap: Bitmap): Result =
        if (isInternetConnection) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val user = userDao.get(queryPreferences.userId)
            val header = mapOf(
                AuthApi.HEADER_ID_USER to userAccount.id.toString(),
                AuthApi.HEADER_PASSWORD_USER to userAccount.password
            )
            val response = fileApi.saveUserPic(
                header,
                FileResponse(
                    user.id,
                    user.pic,
                    outputStream.toByteArray()
                )
            ).execute()
            if (response.code() == 200) {
                Result(Result.State.OK)
            } else {
                Result(
                    Result.State.ERROR,
                    error = Exception(response.errorBody()?.string() ?: "")
                )
            }
        } else {
            Result(
                Result.State.ERROR,
                error = ConnectException("Нет подключения к интернету!")
            )
        }

    fun setName(name: String): Completable =
         try {
            val user = userDao.get(queryPreferences.userId)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val newUser = UserEntity(
                user.id,
                name,
                user.weight,
                user.height,
                user.birthday,
                user.gender,
                user.pic
            )
            stompHelper.updateUser(newUser,userAccount.id,userAccount.password)
        } catch (e: Exception) {
            Completable.error(e)
        }


    fun setEmail(email: String) {

    }

    fun setWeight(weight: Double):Completable =
        try {
            val user = userDao.get(queryPreferences.userId)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val newUser = UserEntity(
                user.id,
                user.name,
                weight,
                user.height,
                user.birthday,
                user.gender,
                user.pic
            )
            stompHelper.updateUser(newUser,userAccount.id,userAccount.password)
        } catch (e: Exception) {
            Completable.error(e)
        }

    fun setHeight(height: Int):Completable =
        try {
            val user = userDao.get(queryPreferences.userId)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val newUser = UserEntity(
                user.id,
                user.name,
                user.weight,
                height,
                user.birthday,
                user.gender,
                user.pic
            )
            stompHelper.updateUser(newUser,userAccount.id,userAccount.password)
        } catch (e: Exception) {
            Completable.error(e)
        }

    fun setDate(date: Long):Completable =
        try {
            val user = userDao.get(queryPreferences.userId)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val newUser = UserEntity(
                user.id,
                user.name,
                user.weight,
                user.height,
                date,
                user.gender,
                user.pic
            )
            stompHelper.updateUser(newUser,userAccount.id,userAccount.password)
        } catch (e: Exception) {
            Completable.error(e)
        }


}