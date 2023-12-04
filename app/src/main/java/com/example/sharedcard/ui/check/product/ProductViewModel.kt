package com.example.sharedcard.ui.check.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.repository.ProductRepository

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepository: ProductRepository
    private val accountId: Long
    val productItemLiveData: LiveData<List<Product>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        accountId = (application as SharedCardApp).getQueryPreferences().accountId
        productRepository = application.getProductRepository()
        mutableSearch.value = ""
        productItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                productRepository.getAllCheck()
            } else {
                productRepository.getAllQuery(query = query)
            }
        }
    }

    fun setCheckBox(id: Long, isChecked: Boolean) {
        val status = if (isChecked) 1 else 0
        productRepository.setStatus(id, status)
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}