package com.example.sharedcard.ui.products

import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.DictionaryRepository
import javax.inject.Inject

class ProductCategoriesViewModel @Inject constructor(private val repository: DictionaryRepository) :
    ViewModel() {
    fun getProductCategories() =
        repository.getAllCategoriesProduct()

}