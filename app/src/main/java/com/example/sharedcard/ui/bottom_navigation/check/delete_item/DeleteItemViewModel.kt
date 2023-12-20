package com.example.sharedcard.ui.bottom_navigation.check.delete_item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.TargetRepository

class DeleteItemViewModel(application: Application) : AndroidViewModel(application) {
    private val checkRepository: CheckRepository
    private val targetRepository: TargetRepository

    var page = 0

    init {
        checkRepository = (application as SharedCardApp).getProductRepository()
        targetRepository = application.getTargetRepository()
    }

    fun deleteItem(id: Long) {
        when (page) {
            0 -> checkRepository.delete(id)
            1 -> targetRepository.delete(id)
        }
    }
}