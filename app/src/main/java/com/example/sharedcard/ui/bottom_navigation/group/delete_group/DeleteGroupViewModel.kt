package com.example.sharedcard.ui.bottom_navigation.group.delete_group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.GroupUsersRepository
import com.example.sharedcard.repository.TargetRepository
import java.util.UUID

class DeleteGroupViewModel(application: Application) : AndroidViewModel(application) {
    private val groupUsersRepository: GroupUsersRepository


    init {
        groupUsersRepository = (application as SharedCardApp).getGroupUsersRepository()
    }

    fun delete(id: UUID) {
        groupUsersRepository.deleteGroup(id)
    }
}