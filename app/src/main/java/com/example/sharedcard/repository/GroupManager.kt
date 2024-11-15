package com.example.sharedcard.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.entity.gpoup_users.GroupUsers
import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.api.FileApi
import com.example.sharedcard.service.api.GroupApi
import com.example.sharedcard.service.dto.CreateGroupResponse
import com.example.sharedcard.service.dto.FileResponse
import com.example.sharedcard.service.dto.JoinInGroupResponse
import com.example.sharedcard.service.dto.TokenResponse
import com.example.sharedcard.service.stomp.StompHelper
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupManager @Inject constructor(
    private val groupUsersDao: GroupUsersDao,
    private val groupDao: GroupDao,
    private val userDao: UserDao,
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
    fun deleteGroup(isInternetConnection: Boolean, groupId: UUID): Completable =
        if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.deleteGroup(groupId, queryPreferences.userId, userPassword)
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun isLocalGroup(): Boolean = queryPreferences.isLocal
    fun getCurrentGroupId(): UUID = queryPreferences.groupId

    fun getCurrentGroup(): LiveData<GroupEntity> =
        groupChangedLiveData.switchMap {
            groupDao.getGroupLiveData(it)
        }


    fun setGroupToLocal() {
        val groupUsers = groupDao.findLocalGroup(queryPreferences.userId)
        queryPreferences.groupId = groupUsers.idGroup
        queryPreferences.isLocal = true
        mutableGroupChangedLiveData.postValue(groupUsers.idGroup)
    }

    fun setGroup(idGroup: UUID) {
        queryPreferences.groupId = idGroup
        queryPreferences.isLocal = false
        mutableGroupChangedLiveData.postValue(idGroup)
    }

    fun getGroup(id: UUID = queryPreferences.groupId): LiveData<GroupEntity> =
        groupDao.getGroupLiveData(id)

    fun getAllGroups(): LiveData<List<GroupUsers>> = groupUsersDao.allGroup(queryPreferences.userId)
    fun getStatus(idGroup: UUID) = groupUsersDao.getStatus(queryPreferences.userId, idGroup)
    fun deleteUser(isInternetConnection: Boolean, userId: UUID, groupId: UUID): Completable =
        if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.deleteUser(userId, groupId, queryPreferences.userId, userPassword)
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun setUserStatus(isInternetConnection: Boolean, userId: UUID, groupId: UUID,status: Int): Completable =
        if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.setUserStatus(userId, groupId,status, queryPreferences.userId, userPassword)
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }


    fun joinInGroup(isInternetConnection: Boolean, token: String): Completable =
        if (isInternetConnection) {
            val joinGroupResponse = JoinInGroupResponse(token, queryPreferences.userId)
            try {
                val response = groupApi.joinGroupSync(joinGroupResponse).execute()
                if (response.code() == 200) {
                    Completable.complete()
                } else {
                    Completable.error(Exception(response.errorBody()!!.string()))
                }
            } catch (e: Exception) {
                Completable.error(e)
            }

        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun createGroup(isInternetConnection: Boolean, name: String, pic: Bitmap): Completable =
        if (isInternetConnection) {
            val outputStream = ByteArrayOutputStream()
            pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            val userPassword = userDao.getAccount(queryPreferences.userId).password

            val response =
                CreateGroupResponse(name, queryPreferences.userId, outputStream.toByteArray())
            stompHelper.connect(queryPreferences.userId, userPassword)
            val exec =
                groupApi.createGroup(queryPreferences.userId, userPassword, response).execute()
            if (exec.code() == 200) {
                Completable.complete()
            } else {
                Completable.error(Exception(exec.errorBody()?.string() ?: ""))
            }
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    //TRY/CATCH НАПИШИ!!!!!!
    fun editGroupPic(isInternetConnection: Boolean, id: UUID, pic: Bitmap): Completable =
        if (isInternetConnection) {
            val outputStream = ByteArrayOutputStream()
            pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            val userAccount = userDao.getAccount(queryPreferences.userId)
            val header = mapOf(
                AuthApi.HEADER_ID_USER to userAccount.id.toString(),
                AuthApi.HEADER_PASSWORD_USER to userAccount.password
            )
            val group = groupDao.getGroup(id)
            try {
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
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun getToken(isInternetConnection: Boolean, groupId: UUID): TokenResponse? {
        if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            val response =
                groupApi.getToken(groupId, queryPreferences.userId, userPassword).execute()
            if (response.code() == 200) {
                return response.body()
            } else {
                throw Exception(response.code().toString() + " " + response.errorBody()?.string())
            }
        } else {
            throw ConnectException("Нет подключения к интернету!")
        }
    }

    fun editGroup(isInternetConnection: Boolean, groupId: UUID, name: String): Completable =
        if (isInternetConnection) {
            val userAccount = userDao.getAccount(queryPreferences.userId)
            try {
                stompHelper.updateGroup(groupId, name, userAccount.id, userAccount.password)
            } catch (e: Exception) {
                Completable.error(e)
            }
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun getMyId() = queryPreferences.userId

}