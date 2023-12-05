package com.example.sharedcard.ui.check.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.repository.TargetRepository

class DeleteItemViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository: ProductRepository
    private val targetRepository: TargetRepository

    var page = 0

    init {
        productRepository = (application as SharedCardApp).getProductRepository()
        targetRepository = application.getTargetRepository()
    }

    fun deleteItem(id: Long) {
        when (page) {
            0 -> productRepository.delete(id)
            1 -> targetRepository.delete(id)
        }
    }
}