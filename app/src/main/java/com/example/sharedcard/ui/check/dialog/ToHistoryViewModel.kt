package com.example.sharedcard.ui.check.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.repository.TargetRepository

class ToHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dictionaryRepository: DictionaryRepository
    private val productRepository: ProductRepository
    private val targetRepository: TargetRepository

    var page = 0
    var name = ""
    var shop = 0L
    var price = 0
    var id = 0L

    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
        productRepository = application.getProductRepository()
        targetRepository = application.getTargetRepository()
    }

    var currency = 0L

    fun getShop() =
        when (page) {
            0 -> dictionaryRepository.getAllShopsProduct()
            1 -> dictionaryRepository.getAllShopsTarget()
            else -> throw IndexOutOfBoundsException()
        }

    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun toHistory() {
        when (page) {
            0 -> productRepository.toHistory(id, shop, price, currency)
            1 -> targetRepository.toHistory(id,shop,price,currency)
        }

    }


}