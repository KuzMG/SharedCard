package com.example.sharedcard.ui.check.to_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

class ToHistoryViewModel(
    val page: Int,
    private val id: UUID,
    val name: String,
    private val dictionaryRepository: DictionaryRepository,
    private val checkRepository: CheckRepository,
    private val targetRepository: TargetRepository
) : ViewModel() {


    var price = 0
    var currency = 0L
    var shop = 0L
    fun getShop() =
        when (page) {
            0 -> dictionaryRepository.getAllShopsProduct()
            1 -> dictionaryRepository.getAllShopsTarget()
            else -> throw IndexOutOfBoundsException()
        }

    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun toHistory() {
        if (currency == 0L)
            currency = when (Locale.getDefault().country) {
                "RU" -> 1L
                else -> 2L
            }
        when (page) {
            0 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    checkRepository.toHistory(id, shop, price, currency)
                }
            }

            1 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    targetRepository.toHistory(id, shop, price, currency)
                }
            }
        }
    }


    class ViewModelFactory @AssistedInject constructor(
        @Assisted("page") private val page: Int,
        @Assisted("id") private val id: String,
        @Assisted("name") private val name: String,
        private val dictionaryRepository: DictionaryRepository,
        private val checkRepository: CheckRepository,
        private val targetRepository: TargetRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ToHistoryViewModel::class.java)
            return ToHistoryViewModel(
                page,
                UUID.fromString(id),
                name,
                dictionaryRepository,
                checkRepository,
                targetRepository
            ) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(
            @Assisted("page") page: Int,
            @Assisted("id") id: String,
            @Assisted("name") name: String
        ): ViewModelFactory
    }

}