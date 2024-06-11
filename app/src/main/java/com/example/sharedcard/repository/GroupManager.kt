package com.example.sharedcard.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.gpoup_users.GroupUsers
import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.ui.group.create_group.CreateGroupViewModel
import com.example.sharedcard.ui.group.data.Result
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupManager @Inject constructor(
    private val groupUsersDao: GroupUsersDao,
    private val groupDao: GroupDao,
    private val queryPreferences: QueryPreferences
) {
    val groupChangedLiveData: LiveData<UUID>
        get() = mutableGroupChangedLiveData
    private val mutableGroupChangedLiveData = MutableLiveData<UUID>()
    fun deleteGroup(idGroup: UUID) {
        groupUsersDao.deleteGroup(idGroup)
        groupDao.deleteGroup(idGroup)
    }
    fun isLocalGroup(): Boolean = queryPreferences.isLocal
    fun getCurrentGroupId(): UUID = queryPreferences.groupId


    fun setGroupToLocal(){
        queryPreferences.groupId = groupDao.findLocalGroup(queryPreferences.userId)
        queryPreferences.isLocal = true
        mutableGroupChangedLiveData.postValue(queryPreferences.groupId)
    }

    fun setGroup(idGroup: UUID){
        queryPreferences.groupId = idGroup
        queryPreferences.isLocal = false
        mutableGroupChangedLiveData.postValue(queryPreferences.groupId)
    }
    fun getGroup(id: UUID = queryPreferences.groupId): LiveData<GroupEntity> = groupDao.getGroup(id)
    fun getAllGroups(): LiveData<List<GroupUsers>> = groupUsersDao.allGroup(queryPreferences.userId)
    fun isAdmin(idGroup: UUID) = groupUsersDao.getStatus(queryPreferences.userId, idGroup)
    fun deleteUser(idUser: UUID, idGroup: UUID) {

    }

    fun makeUserAdmin(idUser: UUID, idGroup: UUID) {

    }
    fun addUserInGroup(){
        val groupUsers = GroupUsersEntity(UUID.fromString("3f1c6374-2398-4c57-8e41-37976cd6a88f"),UUID.fromString("9489013c-5a4a-48d9-8bb4-ac29e4e4666b"),false)
        groupUsersDao.createGroup(groupUsers)
    }

    fun selectGroup(idGroup: UUID){
        queryPreferences.groupId = idGroup
        queryPreferences.isLocal = false
    }

    fun createGroup(name: String, photo: Bitmap): Result {
        val outputStream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        val group = GroupEntity(name = name)
        val groupUsers = GroupUsersEntity(queryPreferences.userId, group.id, true)
        groupDao.createGroup(group)
        groupUsersDao.createGroup(groupUsers)
        return Result(Result.State.OK)
    }



    fun editGroup(id: UUID, name: String, photo: Bitmap?): Result {
        groupDao.setName(id, name)
        return Result(Result.State.OK)
    }


}