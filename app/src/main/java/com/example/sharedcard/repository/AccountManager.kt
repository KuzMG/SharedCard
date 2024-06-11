package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.retrofit.api.GroupApi
import com.example.sharedcard.retrofit.api.UserApi
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AccountManager @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val groupUsersDao: GroupUsersDao,
    private val groupApi: GroupApi,
    private val userApi: UserApi,
    private val queryPreferences: QueryPreferences
) {


    fun getUser() = userDao.get(queryPreferences.userId)
    fun accountExists() = queryPreferences.userId != UUID.fromString(QueryPreferences.DEF_VALUE)

    fun signIn(email: String, password: String): Boolean {
        val user = userDao.findUser(email)
        if(user.password != password)
            return false
        val idGroup = groupDao.findLocalGroup(user.id)
        queryPreferences.run {
            userId = user.id
            groupId = idGroup
            isLocal = true
        }
        return true
    }

    fun signUp(email: String, password: String) {
        val user = UserEntity(email = email, name = "user", password = password)
        val group = GroupEntity(name = "")
        userDao.createUser(user)
        groupDao.createGroup(group)
        groupUsersDao.createGroup(GroupUsersEntity(user.id, group.id, true))
        queryPreferences.run {
            userId = user.id
            groupId = group.id
            isLocal = true
        }
    }

    fun exitFromAccount() {
        queryPreferences.groupId = UUID.fromString(QueryPreferences.DEF_VALUE)
        queryPreferences.userId = UUID.fromString(QueryPreferences.DEF_VALUE)
    }

    fun setPhoto() {

    }

    fun setName(name: String) {
        userDao.setName(queryPreferences.userId, name)
    }

    fun setEmail(email: String) {

    }

    fun setWeight(weight: Int) {
        userDao.setWeight(queryPreferences.userId, weight)
    }

    fun setHeight(height: Int) {
        userDao.setHeight(queryPreferences.userId, height)
    }

    fun setAge(age: Int) {
        userDao.setAge(queryPreferences.userId, age)
    }


}