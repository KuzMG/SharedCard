package com.example.sharedcard.ui.check.add_target

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import com.project.shared_card.database.dao.target.TargetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class AddTargetViewModel @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val dictionaryRepository: DictionaryRepository,
    private val targetRepository: TargetRepository
) : ViewModel() {
    private val idGroup: UUID
        get() = queryPreferences.groupId
    private val idUser: UUID
        get() = queryPreferences.userId
    private val isLocal: Boolean
        get() = queryPreferences.isLocal

    var name = ""
    var price = 0
    var currency = 0L
    var category = 0L


    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun getCategory() = dictionaryRepository.getAllCategoriesTarget()

    fun check() = when {
        name.isEmpty() -> 1
        else -> 2
    }

    fun add() {
        if(currency==0L){
            currency = when(Locale.getDefault().country){
                "RU" -> 1L
                else -> 2L
            }
        }
        val target = TargetEntity(
            name = name,
            firstPrice = price,
            idCurrencyFirst = currency,
            idCategory = category,
            idGroup = idGroup,
            idCreator = idUser
        )
        if (isLocal)
            viewModelScope.launch(Dispatchers.IO) {
                targetRepository.add(target)
            }
    }

}