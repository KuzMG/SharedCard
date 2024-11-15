package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.gpoup_users.GroupUsersDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.user.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val groupUsersDao: GroupUsersDao,
) {
}