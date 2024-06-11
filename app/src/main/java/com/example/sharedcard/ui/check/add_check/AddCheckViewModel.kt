package com.example.sharedcard.ui.check.add_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class AddCheckViewModel @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val dictionaryRepository: DictionaryRepository,
    private val checkRepository: CheckRepository
) : ViewModel() {
    private val idGroup: UUID
        get() = queryPreferences.groupId
    private val idUser: UUID
        get() = queryPreferences.userId
    private val isLocal: Boolean
        get() = queryPreferences.isLocal
    var count = 1
    var metric = 1L
    var description = ""
    var product: ProductEntity? = null


    fun getMetric() = dictionaryRepository.getAllMetrics()


    fun getProducts(query: String) = dictionaryRepository.getProductByQuery(query)
    fun check() = when (product) {
        null -> 1
        else -> 2
    }

    fun add() {
        val check = CheckEntity(
            idProduct = product!!.id,
            description = description,
            idMetric = metric,
            count = count,
            idCreator = idUser,
            idGroup = idGroup
        )
        viewModelScope.launch(Dispatchers.IO) {
            checkRepository.add(check)
        }
    }

}