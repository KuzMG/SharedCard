package com.example.sharedcard.ui.purchase.add_purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.PurchaseManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class AddPurchaseViewModel(
    query: String,
    private val dictionaryRepository: DictionaryRepository,
    private val purchaseManager: PurchaseManager,
    private val groupManager: GroupManager
) : ViewModel() {
    val newProductLiveData: LiveData<Product>
        get() = newProductMutableLiveData
    private val newProductMutableLiveData = MutableLiveData<Product>()
    var selectableCountChipIndex: Int = 0
    var selectablePriceChipIndex: Boolean = false
    val resultLiveData: LiveData<Result<Boolean>>
        get() = resultMutableLiveData
    private val resultMutableLiveData = MutableLiveData<Result<Boolean>>()
    var currency = ""

    val listMetricOne = listOf("0.1", "0.2", "0.5", "1")
    val listMetricTwo = listOf("0.5", "1", "1.5", "2")
    val listMetricThree = listOf("1", "2", "3", "5")
    var selectableListMetric = listMetricOne


    var product: Product? = null
        set(value) {
            field = value
            selectableCountChipIndex = 0
            value?.let {
                newProductMutableLiveData.value = it
            }
        }
    var group: UUID = groupManager.getDefaultGroup()
    var count = 0.0
    var price = 0.0
    var description = ""
    val productsLiveData: LiveData<List<Product>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = query
        productsLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                dictionaryRepository.getProductPopular()
            } else {
                dictionaryRepository.getProductByQuery(query)
            }
        }
    }

    fun getCurrency() = dictionaryRepository.getCurrency()


    //
    fun setQuery(query: String) {
        mutableSearch.value = query
    }

    fun add() {
        viewModelScope.launch(Dispatchers.IO) {
            product?.let {
                purchaseManager.add(it.productEntity.id, description, count, price, group)
                    .blockingGet()?.let { e ->
                        resultMutableLiveData.postValue(Result.failure(e))
                        return@launch
                    }
                resultMutableLiveData.postValue(Result.success(true))
            }
        }
    }

    fun getGroups() = groupManager.getGroups()

    class ViewModelFactory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val dictionaryRepository: DictionaryRepository,
        private val purchaseManager: PurchaseManager,
        private val groupManager: GroupManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AddPurchaseViewModel::class.java)
            return AddPurchaseViewModel(
                query,
                dictionaryRepository,
                purchaseManager,
                groupManager
            ) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted("query") query: String): ViewModelFactory
    }
}