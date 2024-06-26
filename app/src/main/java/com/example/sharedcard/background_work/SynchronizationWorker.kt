package com.example.sharedcard.background_work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.retrofit.api.SynchronizationApi

class SynchronizationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context,
    workerParams
) {
//    private val synchronizationApi = SharedCardService.getInstance().create(SynchronizationApi::class.java)
//    private val dictionaryRepository = (applicationContext as SharedCardApp).getDictionaryRepository()
    override suspend fun doWork(): Result {
//        synchronizationApi.apply {
//            getCategories().execute().body()?.let { dictionaryRepository.addCategories(it) }
//            getProducts().execute().body()?.let { dictionaryRepository.addProducts(it) }
//            getCurrencies().execute().body()?.let {dictionaryRepository.addCurrencies(it)}
//            getShops().execute().body()?.let { dictionaryRepository.addShops(it) }
//            getMetrics().execute().body()?.let { dictionaryRepository.addMetrics(it) }
//        }
        return Result.success()
    }
}



