package com.example.sharedcard.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.purchase.filter.enums.GROUPING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORTING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORT_MODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.math.ceil

class PurchaseViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val purchaseManager: PurchaseManager,
    private val groupManager: GroupManager
) : ViewModel() {
    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData

    var excludeGroupsSet = mutableSetOf<UUID>()
    var excludePersonsSet = mutableSetOf<UUID>()

    var sortMode = SORT_MODE.ASC
    var sortingBy = SORTING_BY.CATEGORY
    var groupingBy = GROUPING_BY.GROUP

    val filterLiveData: LiveData<Boolean>
        get() = _filterLiveData
    private val _filterLiveData = MutableLiveData<Boolean>()

    val basketSwipeLiveData: LiveData<Pair<Int,Throwable>?>
        get() = _basketSwipeLiveData
    private val _basketSwipeLiveData = MutableLiveData<Pair<Int,Throwable>?>()
    val purchaseSwipeLiveData: LiveData<Pair<Int,Throwable>?>
        get() = _purchaseSwipeLiveData
    private val _purchaseSwipeLiveData = MutableLiveData<Pair<Int,Throwable>?>()
    val groupChanged: LiveData<UUID>
        get() = groupManager.groupChangedLiveData

    private val mutableSearch = MutableLiveData<String>()

    init {
        _filterLiveData.value = true
        mutableSearch.value = ""
    }

    fun getPurchases(id: UUID) = mutableSearch.switchMap { query ->
        purchaseManager.getByFilter(groupingBy,id,sortingBy,sortMode,excludeGroupsSet,excludePersonsSet,query)
    }

    fun getGroupsItem() =
        when (groupingBy) {
            GROUPING_BY.GROUP -> groupManager.getGroups(excludeGroupsSet)
            GROUPING_BY.PERSON -> groupManager.getPersonsWithoutYou(excludePersonsSet)
        }


    fun getBaskets(purchaseId: UUID) = purchaseManager.getBaskets(purchaseId)

    fun getGroupsWithoutDefault() = groupManager.getGroupsWithoutDefault()
    fun filterComplete(){
        _filterLiveData.value = true
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }

    fun deletePurchase(groupId: UUID, purchaseId: UUID,position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            purchaseManager.delete(groupId,purchaseId).blockingGet()?.let {e ->
                _purchaseSwipeLiveData.postValue(position to e)
                return@launch
            }
            _purchaseSwipeLiveData.postValue(null)
        }
    }

    private fun generateSetKg(c: Double): Set<Double> {
        val set = sortedSetOf<Double>()
        set.add(1.0)
        set.add(c / 5)
        set.add(c / 4)
        set.add(c / 2)
        set.add(c)
        return set
    }

    private fun generateSetL(c: Double): Set<Double> {
        val set = sortedSetOf<Double>()
        set.add(1.0)
        set.add(0.5)
        if (c > 1.5) set.add(1.5)
        if (c > 2.0) set.add(2.0)
        set.add(c)
        return set
    }

    private fun generateSetPiece(c: Double): Set<Double> {
        val set = sortedSetOf<Double>()
        set.add(0.1)
        if (c > 0.5) set.add(c / 5)
        if (c > 0.4) set.add(c / 4)
        if (c > 0.2) set.add(c / 2)
        set.add(c)
        return set
    }
    private fun generateSetPiece(c: Int): Set<Double> {
        val set = sortedSetOf<Double>()
        set.add(1.0)
        if (c > 5) set.add(ceil(c.toDouble() / 5))
        if (c > 4) set.add(ceil(c.toDouble() / 4))
        if (c > 2) set.add(ceil(c.toDouble() / 2))
        set.add(c.toDouble())
        return set
    }

    fun selectSetMetric(metric: String,count: Double) = when (metric) {
        "шт" -> {
            generateSetPiece(count.toInt())
        }

        "кг" -> {
            if (count < 1) {
                generateSetPiece(count)
            } else {
                generateSetKg(count)
            }
        }

        "л" -> {
            if (count < 1) {
                generateSetPiece(count)
            } else {
                generateSetL(count)
            }
        }

        else -> throw IndexOutOfBoundsException()
    }
    fun addBasket(id: UUID, count: Double, groupId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            purchaseManager.addBasket(id, count, groupId).blockingGet()?.let {
                _sendLiveData.postValue(it)
            }
        }
    }

    fun deleteBasket(basketId: UUID,groupId: UUID,position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            purchaseManager.deleteBasket(basketId,groupId).blockingGet()?.let {
                _basketSwipeLiveData.postValue(position to it)
                return@launch
            }
            _basketSwipeLiveData.postValue(null)

        }

    }

    fun getCountBasket(purchaseId: UUID) =purchaseManager.getCountBasket(purchaseId)
    fun toHistory(groupId: UUID, basketId: UUID, price: Double, shopId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            purchaseManager.basketToHistory(groupId,basketId,shopId, price).subscribe({

            },{e ->

            })
        }
    }

    fun getShops() =  dictionaryRepository.getAllShops()

    fun purchaseToHistory(groupId:UUID,purchaseId: UUID,position: Int) {
        viewModelScope.launch(Dispatchers.IO){
            purchaseManager.purchaseToHistory(groupId,purchaseId).blockingGet()?.let {
                _purchaseSwipeLiveData.postValue(position to it)
                return@launch
            }
            _purchaseSwipeLiveData.postValue(null)
        }

    }

    fun getPersons() = groupManager.getPersonsWithoutYou()
    fun getCurrency(basketId: UUID) = dictionaryRepository.getCurrencyByBasketId(basketId)



}