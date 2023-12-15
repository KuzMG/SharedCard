package com.example.sharedcard.ui.bottom_navigation.check.add_item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.TargetRepository
import com.project.shared_card.database.dao.target.TargetEntity

class AddItemViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dictionaryRepository: DictionaryRepository
    private val checkRepository: CheckRepository
    private val targetRepository: TargetRepository

    var page = 0
    var name = ""
    var category = 0L
    var count = 0
    var metric = 0L

    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
        checkRepository = application.getProductRepository()
        targetRepository = application.getTargetRepository()
    }
//    fun getCategory() =
//        when (page) {
//            0 -> dictionaryRepository.getAllCategoriesProduct()
//            test.jpg -> dictionaryRepository.getAllCategoriesTarget()
//            else -> throw IndexOutOfBoundsException()
//        }

    fun getMetric() =
        when (page) {
            0 -> dictionaryRepository.getAllMetrics()
            1 -> dictionaryRepository.getAllCurrency()
            else -> throw IndexOutOfBoundsException()
        }

//    fun add() {
//        when (page) {
//            0 -> {
//                val product = CheckEntity(
//                    name = name,
//                    idCategory = category,
//                    count = count,
//                    idMetric = metric
//                )
//                checkRepository.add(product)
//            }
//
//            test.jpg -> {
//                val target = TargetEntity(
//                    name = name,
//                    idCategory = category,
//                    firstPrice = count,
//                    idCurrencyFirst = metric
//                )
//                targetRepository.add(target)
//            }
//        }
//    }

}