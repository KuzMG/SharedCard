package com.example.sharedcard.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) :
    ViewModel() {

    private val currencyIdMutableList = MutableLiveData<Int>()
    var currencyId: Int = dictionaryRepository.getCurrencyId()
        set(value) {
            field = value
            dictionaryRepository.setCurrency(value)
            currencyIdMutableList.value = value
        }

    init {
        currencyId = dictionaryRepository.getCurrencyId()
    }

    fun getCurrency() = currencyIdMutableList.switchMap {
        dictionaryRepository.getCurrency()
    }

    fun getCurrencies() = dictionaryRepository.getAllCurrency()


}