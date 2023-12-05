package com.example.sharedcard.ui.check.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.repository.TargetRepository
import com.project.shared_card.database.dao.target.TargetEntity

class AddItemViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dictionaryRepository: DictionaryRepository
    private val productRepository: ProductRepository
    private val targetRepository: TargetRepository

    var page = 0
    var name = ""
    var category = 0L
    var count = 0
    var metric = 0L

    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
        productRepository = application.getProductRepository()
        targetRepository = application.getTargetRepository()
    }
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
                val target = TargetEntity(
                    name = name,
                    idCategory = category,
                    firstPrice = count,
                    idCurrencyFirst = metric
                )
                targetRepository.add(target)
            }
        }
    }

}