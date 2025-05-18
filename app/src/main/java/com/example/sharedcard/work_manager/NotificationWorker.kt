package com.example.sharedcard.work_manager

import android.app.ActivityManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.notification.NotificationHelper
import com.example.sharedcard.notification.NotificationItem
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.repository.StompConnectionManager
import com.example.sharedcard.service.dto.AccountResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.toStringFormat
import com.google.gson.Gson
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var accountManager: AccountManager

    @Inject
    lateinit var dictionaryRepository: DictionaryRepository

    @Inject
    lateinit var purchaseManager: PurchaseManager

    @Inject
    lateinit var stompHelper: StompHelper

    init {
        context.appComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        if (!isApplicationInBackground(applicationContext)) {
            val account = accountManager.getAccount()
            val dssd = stompHelper.subscribeOnSyncFull(account.id)
            stompHelper.connect(account.id,account.password)

            dssd.blockingForEach{msg ->
                val accountResponse = Gson().fromJson(msg.payload, AccountResponse::class.java)
                val purchases = purchaseManager.getAll()
                val newPurchases = accountResponse.purchases.filter { responsePurchase ->
                    !purchases.contains(responsePurchase)
                }
                handler(newPurchases)
            }
            stompHelper.disconnect()
        }
        return Result.success()
    }

    private fun handler(purchases: List<PurchaseEntity>) {
        val list = purchases.map { purchase ->
            val product = dictionaryRepository.getProductById(purchase.idProduct)
            val currency = dictionaryRepository.getCurrencyById(purchase.idCurrency)
            val person = accountManager.getPerson()
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
        NotificationHelper.showUserProductNotification(applicationContext, list)
    }

    private fun isApplicationInBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = activityManager.runningAppProcesses ?: return true
        for (processInfo in runningProcesses) {
            if (processInfo.processName == context.packageName) {
                return processInfo.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return true
    }
}

