package com.example.sharedcard.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.entity.gpoup_person.GroupPersons
import com.example.sharedcard.database.entity.gpoup_person.GroupPersonsDao
import com.example.sharedcard.database.entity.gpoup_person.UserEntityWithStatus
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.person.PersonDao
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.api.FileApi
import com.example.sharedcard.service.api.GroupApi
import com.example.sharedcard.service.dto.CreateGroupResponse
import com.example.sharedcard.service.dto.FileResponse
import com.example.sharedcard.service.dto.JoinInGroupResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.ui.group.token_group.TokenResult
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupManager @Inject constructor(
    private val groupPersonsDao: GroupPersonsDao,
    private val groupDao: GroupDao,
    private val personDao: PersonDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper,
    private val groupApi: GroupApi,
    private val fileApi: FileApi
) {


    val groupChangedLiveData: LiveData<UUID>
        get() = mutableGroupChangedLiveData
    private val mutableGroupChangedLiveData = MutableLiveData<UUID>()

    init {
        mutableGroupChangedLiveData.postValue(queryPreferences.groupId)
    }

    fun deleteGroup(groupId: UUID): Completable = try {
        val password = personDao.getAccount(queryPreferences.personId).password
        stompHelper.deleteGroup(groupId, queryPreferences.personId, password)
    } catch (e: Exception) {
        Completable.error(e)
    }


    fun isLocalGroup(): Boolean = queryPreferences.isLocal
    fun getCurrentGroupId(): UUID = queryPreferences.groupId

    fun getCurrentGroup(): LiveData<GroupEntity> =
        groupChangedLiveData.switchMap {
            groupDao.getGroupLiveData(it)
        }


    fun setGroup(groupId: UUID) {
        queryPreferences.groupId = groupId
        queryPreferences.isLocal = false
        mutableGroupChangedLiveData.postValue(groupId)
    }

    fun getGroup(id: UUID = queryPreferences.groupId): LiveData<GroupEntity> =
        groupDao.getGroupLiveData(id)

    fun getGroups(): LiveData<List<GroupEntity>> =
        groupDao.getAll(queryPreferences.personId)

    fun getGroupsWithoutDefault(): LiveData<List<GroupEntity>> =
        groupDao.getAllWithoutDefault(queryPreferences.personId)

    fun getAllGroups(): LiveData<List<GroupPersons>> =
        groupPersonsDao.allGroup(queryPreferences.personId)

    fun getStatus(groupId: UUID) = groupPersonsDao.getStatus(queryPreferences.personId, groupId)
    fun deletePerson(personId: UUID, groupId: UUID): Completable = try {
        val password = personDao.getAccount(queryPreferences.personId).password
        stompHelper.deletePerson(personId, groupId, queryPreferences.personId, password)
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun setPersonStatus(
        personId: UUID,
        groupId: UUID,
        status: Int
    ): Completable = try {
        val password = personDao.getAccount(queryPreferences.personId).password
        stompHelper.setPersonStatus(
            personId,
            groupId,
            status,
            queryPreferences.personId,
            password
        )
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun joinInGroup(token: String): Completable = try {
        val joinGroupResponse = JoinInGroupResponse(token, queryPreferences.personId)
        val response = groupApi.joinGroupSync(joinGroupResponse).execute()
        if (response.code() == 200) {
            Completable.complete()
        } else {
            Completable.error(Exception(response.errorBody()!!.string()))
        }
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun createGroup(name: String, pic: Bitmap): Completable = try {
        val outputStream = ByteArrayOutputStream()
        pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        val password = personDao.getAccount(queryPreferences.personId).password

        val response =
            CreateGroupResponse(name, queryPreferences.personId, outputStream.toByteArray())
        stompHelper.connect(queryPreferences.personId, password)
        val exec =
            groupApi.createGroup(queryPreferences.personId, password, response).execute()
        if (exec.code() == 200) {
            Completable.complete()
        } else {
            Completable.error(Exception(exec.errorBody()?.string() ?: ""))
        }
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun editGroupPic(id: UUID, pic: Bitmap): Completable = try {
        val outputStream = ByteArrayOutputStream()
        pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        val account = personDao.getAccount(queryPreferences.personId)
        val header = mapOf(
            AuthApi.HEADER_ID_PERSON to account.id.toString(),
            AuthApi.HEADER_PASSWORD_PERSON to account.password
        )
        val group = groupDao.getGroup(id)
        val exec =
            fileApi.saveGroupPic(
                header,
                FileResponse(id, group.pic, outputStream.toByteArray())
            ).execute()
        if (exec.code() == 200) {
            Completable.complete()
        } else {
            Completable.error(Exception(exec.errorBody()?.string() ?: ""))
        }
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun getToken(groupId: UUID) =
        try {
            val password = personDao.getAccount(queryPreferences.personId).password
            val response =
                groupApi.getToken(groupId, queryPreferences.personId, password).execute()
            if (response.code() == 200) {
                TokenResult(token = response.body())
            } else {
                TokenResult(
                    error = Exception(
                        response.code().toString() + " " + response.errorBody()?.string()
                    )
                )
            }
        } catch (e: Exception) {
            TokenResult(error = e)
        }


    fun editGroup(groupId: UUID, name: String): Completable = try {
        val account = personDao.getAccount(queryPreferences.personId)
        stompHelper.updateGroup(groupId, name, account.id, account.password)
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun getDefaultGroup() = queryPreferences.groupId
    fun getMyId() = queryPreferences.personId
    fun getPersonsWithoutYou() = personDao.getAllWithoutYou(queryPreferences.personId)

    fun getPersonsByGroup(groupId: UUID): LiveData<List<UserEntityWithStatus>> =
        personDao.getByGroup(groupId)

    fun getGroups(query:String,excludeGroupsSet: MutableSet<UUID>) =
        groupDao.getAll("%$query%",queryPreferences.personId, excludeGroupsSet)


    fun getPersons(query:String,excludePersonsSet: MutableSet<UUID>) =
        personDao.getAll("%$query%",queryPreferences.personId, excludePersonsSet)

    fun getPersonByIdLiveData(personId: UUID) = personDao.getPersonLiveData(personId)

    fun getPersonById(personId: UUID) = personDao.get(personId)
    fun getGroupById(groupId: UUID) = groupDao.getGroupLiveData(groupId)
    fun testQuery(value: String?, excludeGroupsSet: MutableSet<UUID>)=
        groupDao.testQuery("%$value%")



}