package com.example.sharedcard.ui.settings

import android.app.Application
import android.text.BoringLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.GroupUsersRepository
import com.example.sharedcard.repository.QueryPreferences
import java.util.UUID

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences
    private val groupUsersRepository: GroupUsersRepository
    private val userId: UUID
    var quickDelete: Boolean
        get() = queryPreferences.quickDelete
        set(value){
            queryPreferences.quickDelete = value
        }
    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        groupUsersRepository = application.getGroupUsersRepository()
        userId = queryPreferences.userId
    }

    fun getUser() =groupUsersRepository.getUser(userId)

}