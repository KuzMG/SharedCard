package com.example.sharedcard.ui.check.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.repository.QueryPreferences

class ProductViewModel private constructor(
    private val productRepository: ProductRepository,
    queryPreferences: QueryPreferences
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SharedCardApp
                ProductViewModel(
                    app.getProductRepository(),
                    app.getQueryPreferences()
                )
            }
        }
    }

    private val accountId: Long
    val productItemLiveData: LiveData<List<Product>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        accountId = queryPreferences.accountId
        productItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                productRepository.getAllCheck(accountId)
            } else {
                productRepository.getAllQuery(accountId, query)
            }
        }
    }


    fun setQuery(query: String) {
        mutableSearch.value = query
    }
}