package com.example.sharedcard.ui.check.to_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.CheckManager
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.TargetManager
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
    private val checkManager: CheckManager,
    private val targetManager: TargetManager
) : ViewModel() {

    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData

    var price = 0
    var currency = 0
    var shop = 0
    fun getShop() =
        when (page) {
            0 -> dictionaryRepository.getAllShopsProduct()
            1 -> dictionaryRepository.getAllShopsTarget()
            else -> throw IndexOutOfBoundsException()
        }

    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun toHistory(isInternetConnection: Boolean) {
        if (currency == 0)
            currency = when (Locale.getDefault().country) {
                "RU" -> 1
                else -> 2
            }
        when (page) {
            0 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    checkManager.toHistory(isInternetConnection, id, shop, price, currency)
                        .subscribe({
                            _sendLiveData.postValue(null)
                        }, { error ->
                            _sendLiveData.postValue(error)
                        })
                }
            }

            1 -> {
                viewModelScope.launch(Dispatchers.IO) {
                    targetManager.toHistory(isInternetConnection, id, shop, price, currency)
                        .subscribe({
                            _sendLiveData.postValue(null)
                        }, { error ->
                            _sendLiveData.postValue(error)
                        })
                }
            }
        }
    }


    class ViewModelFactory @AssistedInject constructor(
        @Assisted("page") private val page: Int,
        @Assisted("id") private val id: String,
        @Assisted("name") private val name: String,
        private val dictionaryRepository: DictionaryRepository,
        private val checkManager: CheckManager,
        private val targetManager: TargetManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ToHistoryViewModel::class.java)
            return ToHistoryViewModel(
                page,
                UUID.fromString(id),
                name,
                dictionaryRepository,
                checkManager,
                targetManager
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