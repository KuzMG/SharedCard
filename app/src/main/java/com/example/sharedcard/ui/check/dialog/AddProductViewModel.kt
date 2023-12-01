package com.example.sharedcard.ui.check.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.database.entity.product.ProductEntity

class AddProductViewModel(
    private val dictionaryRepository: DictionaryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SharedCardApp
                AddProductViewModel(
                    app.getDictionaryRepository(),
                    app.getProductRepository()
                )
            }
        }
    }

    var page = 0
    var name = ""
    var category = 0L
    var count = 0
    var metric = 0L
    fun getCategory() =
        when (page) {
            0 -> dictionaryRepository.getAllCategoriesProduct()
            1 -> dictionaryRepository.getAllCategoriesTarget()
            else -> throw IndexOutOfBoundsException()
        }

    fun getMetric() =
        when (page) {
            0 -> dictionaryRepository.getAllMetrics()
            1 -> dictionaryRepository.getAllCurrency()
            else -> throw IndexOutOfBoundsException()
        }

    fun add() {
        when (page) {
            0 -> {
                val product = ProductEntity(
                    name = name,
                    idCategory = category,
                    count = count,
                    idMetric = metric
                )
                productRepository.add(product)
            }

            1 -> {

            }
        }
    }

}