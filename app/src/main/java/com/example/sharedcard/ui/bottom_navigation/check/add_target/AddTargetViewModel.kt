package com.example.sharedcard.ui.bottom_navigation.check.add_target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.UUID

class AddTargetViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences
    private val dictionaryRepository: DictionaryRepository
    private val targetRepository: TargetRepository
    private val idGroup: UUID
        get() = queryPreferences.groupId
    private val idUser: UUID
        get() = queryPreferences.userId
    private val isLocal: Boolean
        get() = queryPreferences.isLocal

    var name = ""
    var price = 0
    var currency = 1L
    var category = 1L

    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
        targetRepository = application.getTargetRepository()
        queryPreferences = application.getQueryPreferences()
    }

    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun getCategory() =  dictionaryRepository.getAllCategoriesTarget()

    fun check() = when {
        name.isEmpty() -> 1
        else -> 2
    }

    fun add() {
        val target = TargetEntity(
            name = name,
            firstPrice = price,
            idCurrencyFirst = currency,
            idCategory = category,
            idGroup = idGroup,
            idCreator = idUser
        )
        if (isLocal) {
            targetRepository.add(target)
        }
    }

}