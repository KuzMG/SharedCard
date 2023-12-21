package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID
import java.util.concurrent.Executors

class GroupUsersRepository private constructor(database: AppDatabase){
    private val groupDao = database.groupDao()
    private val userDao = database.userDao()
    private val groupUsersDao = database.groupUsersDao()
    private val executor = Executors.newSingleThreadExecutor()
    fun createGroup(groups :List<GroupEntity>){
        executor.execute {
            groupDao.createGroups(groups)
        }
    }
    fun createUsers(users :List<UserEntity>){
        executor.execute {
            userDao.createUsers(users)
        }
    }
    fun createGroupUsers(groupUsers :List<GroupUsersEntity>){
        executor.execute {
            groupUsersDao.createGroups(groupUsers)
        }
    }
    fun getUser(id: UUID) = userDao.get(id)
    fun getGroup(id: UUID) = groupDao.getGroup(id)
    fun getAllGroups(id: UUID) = groupUsersDao.allGroup(id)

    fun deleteGroup(id: UUID){

    }
    companion object {
        private var nRepository: GroupUsersRepository? = null
        fun getInstance(database: AppDatabase): GroupUsersRepository {
            if (nRepository == null) nRepository = GroupUsersRepository(database)
            return nRepository!!
        }
    }
}