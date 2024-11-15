package com.example.sharedcard.ui.check.add_check

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.CheckManager
import com.example.sharedcard.repository.DictionaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

class AddCheckViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val checkManager: CheckManager
) : ViewModel() {
    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
    var count = 1
    var metric = 1
    var description = ""
    var product: ProductEntity? = null


    fun getMetric() = dictionaryRepository.getAllMetrics()


    fun getProducts(query: String) = dictionaryRepository.getProductByQuery(query)
    fun check() = when (product) {
        null -> 1
        else -> 2
    }

    fun add(isInternetConnection: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            checkManager.add(
                isInternetConnection = isInternetConnection,
                idProduct = product!!.id,
                description = description,
                idMetric = metric,
                count = count,).subscribe({
                _sendLiveData.postValue(null)
            },{error ->
                _sendLiveData.postValue(error)
            })
        }
    }

}