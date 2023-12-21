package com.example.sharedcard.ui.bottom_navigation.group.edit_group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.GroupUsersRepository
import java.util.UUID

class EditGroupViewModel(application: Application) : AndroidViewModel(application) {
    private val groupUsersRepository: GroupUsersRepository
    var idGroup: UUID = UUID.fromString("")
    init {
        (application as SharedCardApp).apply {
            groupUsersRepository = getGroupUsersRepository()
        }
    }

    fun getGroup() = groupUsersRepository.getGroup(idGroup)
}