package com.example.sharedcard.ui.bottom_navigation.manual.product_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.DictionaryRepository

class ProductListViewModel(application: Application) : AndroidViewModel(application) {
    var idCategory = 0L
    private val dictionaryRepository: DictionaryRepository
    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
    }

    fun getProducts() = dictionaryRepository.getProductById(idCategory)
}