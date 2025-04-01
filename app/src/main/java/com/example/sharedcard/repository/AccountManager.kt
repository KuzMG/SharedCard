package com.example.sharedcard.repository

import android.graphics.Bitmap
import com.example.sharedcard.database.entity.person.PersonDao
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.api.FileApi
import com.example.sharedcard.service.dto.FileResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.ui.group.data.Result
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    private val personDao: PersonDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper,
    private val fileApi: FileApi
) {


    fun getPerson() = personDao.getPersonLiveData(queryPreferences.personId)
    fun getAccountLiveData() = personDao.getAccountLiveData(queryPreferences.personId)
    fun getAccount() = personDao.getAccount(queryPreferences.personId)
    fun accountExists() =
        queryPreferences.personId != UUID.fromString(QueryPreferences.DEF_VALUE) && queryPreferences.isSync


    fun exitFromAccount() {
        queryPreferences.run {
            groupId = UUID.fromString(QueryPreferences.DEF_VALUE)
            personId = UUID.fromString(QueryPreferences.DEF_VALUE)
            isSync = false
        }
    }

    fun setImage(bitmap: Bitmap): Result = try{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        val account = personDao.getAccount(queryPreferences.personId)
        val person = personDao.get(queryPreferences.personId)
        val header = mapOf(
            AuthApi.HEADER_ID_PERSON to account.id.toString(),
            AuthApi.HEADER_PASSWORD_PERSON to account.password
        )
        val response = fileApi.savePersonPic(
            header,
            FileResponse(
                person.id,
                person.pic,
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
    } catch (e:Exception){
        Result(Result.State.ERROR, e)
    }

    fun setName(name: String): Completable =
        try {
            val person = personDao.get(queryPreferences.personId)
            val account = personDao.getAccount(queryPreferences.personId)
            val newPerson = PersonEntity(
                person.id,
                name,
                person.weight,
                person.height,
                person.birthday,
                person.gender,
                person.pic
            )
            stompHelper.updatePerson(newPerson, account.id, account.password)
        } catch (e: Exception) {
            Completable.error(e)
        }


    fun setEmail(email: String) {

    }

    fun setWeight(weight: Double): Completable =
        try {
            val person = personDao.get(queryPreferences.personId)
            val account = personDao.getAccount(queryPreferences.personId)
            val newPerson = PersonEntity(
                person.id,
                person.name,
                weight,
                person.height,
                person.birthday,
                person.gender,
                person.pic
            )
            stompHelper.updatePerson(newPerson, account.id, account.password)
        } catch (e: Exception) {
            Completable.error(e)
        }

    fun setHeight(height: Int): Completable =
        try {
            val person = personDao.get(queryPreferences.personId)
            val account = personDao.getAccount(queryPreferences.personId)
            val newPerson = PersonEntity(
                person.id,
                person.name,
                person.weight,
                height,
                person.birthday,
                person.gender,
                person.pic
            )
            stompHelper.updatePerson(newPerson, account.id, account.password)
        } catch (e: Exception) {
            Completable.error(e)
        }

    fun setDate(date: Long): Completable =
        try {
            val person = personDao.get(queryPreferences.personId)
            val account = personDao.getAccount(queryPreferences.personId)
            val newPerson = PersonEntity(
                person.id,
                person.name,
                person.weight,
                person.height,
                date,
                person.gender,
                person.pic
            )
            stompHelper.updatePerson(newPerson, account.id, account.password)
        } catch (e: Exception) {
            Completable.error(e)
        }


}