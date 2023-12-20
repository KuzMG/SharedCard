package com.example.sharedcard.ui.bottom_navigation.check.add_check

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository

class AddCheckViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences
    private val dictionaryRepository: DictionaryRepository
    private val checkRepository: CheckRepository
    private val idGroup: Long
        get() = queryPreferences.groupId
    private val idUser: Long
        get() = queryPreferences.userId
    private val isLocal: Boolean
        get() = queryPreferences.isLocal
    var count = 1
    var metric = 1L
    var description = ""
    var product: ProductEntity? = null

    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
        checkRepository = application.getProductRepository()
        queryPreferences = application.getQueryPreferences()
    }

    fun getMetric() = dictionaryRepository.getAllMetrics()


    fun getProducts(query: String) = dictionaryRepository.getProductByQuery(query)
    fun check() = when {
        product == null -> 1
        else -> 2
    }

    fun add() {
        val check = CheckEntity(
            idProduct = product!!.id!!,
            description = description,
            idMetric = metric,
            count = count,
            idCreator = idUser,
            idGroup = idUser
        )
        if (isLocal) {
            checkRepository.add(check)
        }
    }

}