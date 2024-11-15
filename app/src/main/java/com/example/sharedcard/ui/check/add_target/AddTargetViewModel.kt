package com.example.sharedcard.ui.check.add_target

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class AddTargetViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val targetManager: TargetManager
) : ViewModel() {
    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
    var name = ""
    var price = 0
    var currency = 0
    var category = 0


    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun getCategory() = dictionaryRepository.getAllCategoriesTarget()

    fun check() = when {
        name.isEmpty() -> 1
        else -> 2
    }

    fun add(isInternetConnection: Boolean) {
        if (currency == 0) {
            currency = when (Locale.getDefault().country) {
                "RU" -> 1
                else -> 2
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            targetManager.add(isInternetConnection, name, price, currency, category).subscribe({
                _sendLiveData.postValue(null)
            },{error ->
                _sendLiveData.postValue(error)
            })
        }
    }

}