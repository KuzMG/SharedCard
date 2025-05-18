package com.example.sharedcard.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.database.entity.statistic.data.CountChart
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.repository.StatisticRepository
import com.example.sharedcard.ui.history.adapter.ViewModelHistoryAdapter
import com.example.sharedcard.ui.statistic.data.ChartDate
import com.google.android.material.tabs.TabLayout.Tab
import org.eazegraph.lib.models.BarModel
import java.util.UUID
import javax.inject.Inject

class StatisticViewModel @Inject constructor(
    private val groupManager: GroupManager,
    private val statisticRepository: StatisticRepository,
    private val purchaseManager: PurchaseManager,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel(),ViewModelHistoryAdapter{
    var dateFirst = 0L
    var dateLast = System.currentTimeMillis()
    private val isDateChangeLiveData = MutableLiveData<Boolean>()
    private val isGroupChangeLiveData = MutableLiveData<UUID?>()
    private val isChartPresentationChangeLiveData = MutableLiveData<ChartDate>()
    var mapGroup = hashMapOf<Tab, UUID>()
    val months = arrayOf("янв", "фев", "мар", "апр", "май","июн","июл","авг","сен","окт", "ноя", "дек",)
    val selectableDateLiveData : LiveData<Long>
        get() = selectableDateMutableLiveData
    private val selectableDateMutableLiveData= MutableLiveData<Long>()
    fun setDate(date: Long){
        selectableDateMutableLiveData.value = date
    }
    private val datePurchaseLiveData = MutableLiveData<CountChart?>()

    init {
        setDateInterval(0, System.currentTimeMillis())
        setCurrentGroup(null)
        setChartPresentation(ChartDate.YEAR)
        setDate(dateLast)
    }

    fun getChartPresentation() = isChartPresentationChangeLiveData.value

    fun setChartPresentation(date: ChartDate) {
        isChartPresentationChangeLiveData.value = date
        setDatePurchases(null)
    }

    fun setDateInterval(dateFirst: Long, dateLast: Long) {
        this.dateFirst = dateFirst
        this.dateLast = dateLast
        isDateChangeLiveData.value = true
        setDatePurchases(null)
    }

    fun setCurrentGroup(groupId: UUID?) {
        isGroupChangeLiveData.value = groupId
        setDatePurchases(null)
    }
    fun setDatePurchases(selectableItem: CountChart?) {
        datePurchaseLiveData.value = selectableItem
    }
    fun getGroups() = groupManager.getGroupsWithoutDefault()
    fun getMostPopularProduct() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId == null) {
                statisticRepository.getMostPopularProduct()
            } else {
                statisticRepository.getMostPopularProduct(groupId, dateFirst, dateLast)
            }
        }
    }

    fun getCountMostPopularProduct() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId != null) {
                statisticRepository.getCountMostPopularProduct(groupId, dateFirst, dateLast)
            } else {
                MutableLiveData()
            }
        }
    }


    fun getMostExpensiveProduct() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId == null) {
                statisticRepository.getMostExpensiveProduct(dateFirst, dateLast)
            } else {
                statisticRepository.getMostExpensiveProduct(groupId, dateFirst, dateLast)
            }
        }
    }

    fun getCountMostExpensiveProduct() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId == null) {
                statisticRepository.getCountMostExpensiveProduct(dateFirst, dateLast)
            } else {
                statisticRepository.getCountMostExpensiveProduct(groupId, dateFirst, dateLast)
            }
        }
    }


    fun getCountPurchase() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId == null) {
                statisticRepository.getCountAndPriceHistoryByPersonId(
                    dateFirst = dateFirst,
                    dateLast = dateLast
                )
            } else {
                statisticRepository.getCountAndPriceHistoryByGroupId(groupId, dateFirst, dateLast)
            }
        }
    }

    fun getAddMostPurchasesPerson() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId != null) {
                statisticRepository.getAddMostPurchasesPerson(groupId, dateFirst, dateLast)
            } else {
                MutableLiveData()
            }
        }
    }

    fun getBuyMostPurchasesPerson() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            if (groupId != null) {
                statisticRepository.getBuyMostPurchasesPerson(groupId, dateFirst, dateLast)
            } else {
                MutableLiveData()
            }
        }
    }

    var countItemChart = 0
    var listChartData = listOf<List<CountChart>>()
    var currentChartData = arrayListOf<BarModel>()
    fun getCountPurchaseChart() = isGroupChangeLiveData.switchMap { groupId ->
        isDateChangeLiveData.switchMap {
            isChartPresentationChangeLiveData.switchMap { datePresentation ->
                val purchaseChart = if (groupId != null) {
                    statisticRepository.getCountPurchaseChartByGroup(
                        groupId,
                        dateFirst,
                        dateLast,
                        datePresentation
                    )
                } else {
                    statisticRepository.getCountPurchaseChartByPerson(
                        dateFirst,
                        dateLast,
                        datePresentation
                    )
                }
                purchaseChart.map {
                    when (datePresentation) {
                        ChartDate.YEAR -> dateYear(it)
                        ChartDate.MONTH -> dateMonths(it)
                        ChartDate.WEEK -> dateWeeks(it)
                        else -> throw IndexOutOfBoundsException()
                    }
                }
            }
        }
    }

    private fun dateWeeks(list: List<CountChart>): List<List<CountChart>> {
        val listData = mutableListOf<MutableList<CountChart>>()
        list.forEachIndexed { i, item ->
            if (item.week.toInt() == 1 || i == 0)
                listData.add(mutableListOf())

            listData.last().add(item)
        }
        listChartData = listData
        return listChartData
    }

    private fun dateMonths(list: List<CountChart>): List<List<CountChart>> {
        val listData = mutableListOf<MutableList<CountChart>>()
        var current = ""
        list.forEach { item ->
            if (current != item.month) {
                listData.add(mutableListOf())
                current = item.month
            }
            listData.last().add(item)
        }
        listChartData = listData
        countItemChart = listChartData.lastIndex
        return listChartData
    }

    private fun dateYear(list: List<CountChart>): List<List<CountChart>> {
        val listData = mutableListOf<MutableList<CountChart>>()
        var current = ""
        list.forEach { item ->
            if (current != item.year) {
                listData.add(mutableListOf())
                current = item.year
            }
            listData.last().add(item)
        }
        listChartData = listData
        countItemChart = listChartData.lastIndex
        return listChartData
    }



    fun getPurchases() = datePurchaseLiveData.switchMap { countChart ->
        countChart ?: return@switchMap MutableLiveData<List<History>>(arrayListOf())
        when (isGroupChangeLiveData.value) {
            null -> statisticRepository.getPurchasesPersonId(countChart, getChartPresentation()!!)
            else -> statisticRepository.getPurchasesGroupId(
                isGroupChangeLiveData.value!!,
                countChart,
                getChartPresentation()!!
            )
        }
    }



    override fun getPurchase(purchaseId: UUID) = purchaseManager.getById(purchaseId)
    override fun getPerson(personId: UUID) = groupManager.getPersonByIdLiveData(personId)
    override fun getShop(shopId: Int) = dictionaryRepository.getShopById(shopId)
    override fun getGroup(groupId: UUID) = groupManager.getGroupById(groupId)

}