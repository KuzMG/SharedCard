package com.example.sharedcard.work_manager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.util.appComponent
import javax.inject.Inject
import kotlin.math.ln

class PopularProductWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    companion object {
        private const val TIME_OF_LIFE = 14L * 24 * 60 * 60 * 1000
    }

    @Inject
    lateinit var purchaseRepository: PurchaseManager

    @Inject
    lateinit var dictionaryRepository: DictionaryRepository

    init {
        context.appComponent.inject(this)
    }

    override suspend fun doWork(): Result {
        val purchases = purchaseRepository.getAllByPersonId()
        val currentTime = System.currentTimeMillis()
        val productScoreMap = mutableMapOf<Int, Float>()
        purchases.forEach { purchase ->
            val timeFactor = 1 - (currentTime - purchase.creationDate).toFloat() / TIME_OF_LIFE
            val score = 1 + (timeFactor.coerceIn(0f, 1f) * 0.5f)
            productScoreMap[purchase.idProduct] =
                productScoreMap.getOrDefault(purchase.idProduct, 0f) + score
        }
        productScoreMap.forEach { (k, v) ->
            dictionaryRepository.updateProductWeight(k, v)
        }
        val products = dictionaryRepository.getAllProduct()
        val categoryScoreMap = mutableMapOf<Int, CategoryStats>()
        products.forEach { product ->
            if (product.popularity > 0) {
                val stats = categoryScoreMap.getOrPut(product.idCategory) {
                    CategoryStats(0f, 0)
                }
                stats.productCount++
                stats.totalWeight += product.popularity
            }
        }
        categoryScoreMap.forEach { (k, v) ->
            val totalWeight = v.totalWeight
            val count = v.productCount
            val newWight = totalWeight / count * ln(totalWeight)
            dictionaryRepository.updateCategoryWeight(k, newWight)
        }
        return Result.success()
    }

    private class CategoryStats(
        var totalWeight: Float,
        var productCount: Int
    )
}

