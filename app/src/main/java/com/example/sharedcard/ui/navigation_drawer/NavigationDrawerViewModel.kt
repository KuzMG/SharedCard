package com.example.sharedcard.ui.navigation_drawer

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.notification.NotificationItem
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.repository.StompConnectionManager
import com.example.sharedcard.ui.group.GroupFragment
import com.example.sharedcard.ui.history.HistoryFragment
import com.example.sharedcard.ui.products.ProductCategoriesFragment
import com.example.sharedcard.ui.settings.SettingsFragment
import com.example.sharedcard.ui.statistic.StatisticFragment
import com.example.sharedcard.util.toStringFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NavigationDrawerViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val stompConnectionManager: StompConnectionManager,
    private val dictionaryRepository: DictionaryRepository,
    private val groupManager: GroupManager,
    private val purchaseManager: PurchaseManager
) : ViewModel() {


    var countPurchase = 0
    val fragments: MutableMap<Int, Fragment> =
        mutableMapOf(
            R.id.products to ProductCategoriesFragment(),
            R.id.statistic to StatisticFragment(),
            R.id.group to GroupFragment(),
            R.id.settings to SettingsFragment(),
            R.id.history to HistoryFragment(),
        )

    val connectingState = stompConnectionManager.connectionLiveData

    init {
        connect()
    }

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            val account = accountManager.getAccount()
            stompConnectionManager.connect(account)
        }
    }

    fun getPerson() = accountManager.getPersonLiveData()

    fun exit() {
        accountManager.exitFromAccount()
    }

    fun getCountPurchase() = purchaseManager.getCountPurchaseByPersonId()

    fun createNotification(
        purchases: List<PurchaseEntity>,
        result: (List<NotificationItem>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = purchases.map { purchase ->
                val product = dictionaryRepository.getProductById(purchase.idProduct)
                val currency = dictionaryRepository.getCurrencyById(purchase.idCurrency)
                val person = groupManager.getPersonById(purchase.idPerson)
                val metric = dictionaryRepository.getMetricById(product.idMetric)
                val status = if (purchase.isBought) "Куплен" else "Добавлен"
                val count = "${purchase.count.toStringFormat()} ${metric.name}"
                val price =
                    if (purchase.price > 0) "${purchase.price.toStringFormat()} ${currency.symbol}" else ""
                val text = "$count $price $status"
                NotificationItem(
                    title = product.name,
                    text = text,
                    subText = person.name,
                    picUrl = person.url

                )
            }
            runBlocking(Dispatchers.Main) {
                result.invoke(list)
            }
        }
    }

    override fun onCleared() {
        stompConnectionManager.disconnect()
        super.onCleared()
    }
}