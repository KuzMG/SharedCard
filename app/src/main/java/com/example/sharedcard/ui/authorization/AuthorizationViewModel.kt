package com.example.sharedcard.ui.authorization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.repository.GroupUsersRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.retrofit.SharedCardService
import com.project.shared_card.database.dao.group_users.GroupUsersEntity

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    var login = ""
    var password = ""
    private val queryPreferences: QueryPreferences
    private val groupUsersRepository: GroupUsersRepository

    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        groupUsersRepository = application.getGroupUsersRepository()
    }
    fun check(): Int{
        if(login.isEmpty() || password.isEmpty()) {
            return 0
        } else{
            return 1
        }
    }

    fun signIn(){
        val group = GroupEntity(name = "")
        val user = UserEntity(name = "Михаил")
        queryPreferences.apply {
            userId = user.id
            groupId =group.id
            isLocal = true
        }
        groupUsersRepository.createGroup(arrayListOf(group))
        groupUsersRepository.createUsers(arrayListOf(user))
        groupUsersRepository.createGroupUsers(arrayListOf(GroupUsersEntity(user.id,group.id,true)))
    }
}