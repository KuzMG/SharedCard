package com.example.sharedcard.ui.bottom_navigation.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.GroupUsersRepository

class GroupViewModel(application: Application) : AndroidViewModel(application) {
    private val groupUsersRepository: GroupUsersRepository
    val userId: Long
    private val groupId: Long

    init {
        (application as SharedCardApp).apply {
            userId = 4// getQueryPreferences().userId
            groupId = getQueryPreferences().groupId
            groupUsersRepository = getGroupUsersRepository()
        }
    }

//    fun createUser() {
//        val users = arrayListOf(
//            UserEntity(name = "Миша"),
//            UserEntity(name = "Маша"),
//            UserEntity(name = "Каша"),
//            UserEntity(name = "Даша"),
//            UserEntity(name = "Саша"),
//            UserEntity(name = "Раша")
//        )
//        groupUsersRepository.createUsers(users)
//    }
//    fun createGroup() {
//        val groups = arrayListOf(
//            GroupEntity(test.jpg,"Семья"),
//            GroupEntity(2,"Друзья"),
//            GroupEntity(3,"Коллеги")
//        )
//        groupUsersRepository.createGroup(groups)
//    }

    //    fun createGroupUsers() {
//        val groups = arrayListOf(
//            GroupUsersEntity(test.jpg,test.jpg,true),
//            GroupUsersEntity(2,test.jpg,false),
//            GroupUsersEntity(3,test.jpg,false),
//            GroupUsersEntity(3,2,true),
//            GroupUsersEntity(4,2,false),
//            GroupUsersEntity(5,2,false),
//            GroupUsersEntity(4,3,true),
//            GroupUsersEntity(5,3,false),
//            GroupUsersEntity(6,3,false),
//        )
//        groupUsersRepository.createGroupUsers(groups)
//    }
    fun getGroups() = groupUsersRepository.getAllGroup(userId)
}