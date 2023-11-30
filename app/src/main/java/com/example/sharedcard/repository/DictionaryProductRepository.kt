package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.project.shared_card.database.dao.category_product.CategoryProductEntity
import com.project.shared_card.database.dao.shop_product.ShopProductEntity
import java.util.Locale.Category

class DictionaryProductRepository private constructor(private val database: AppDatabase) {
    val shopProductDao = database.shopProductDao()
    val categoryProductDao = database.categoryProductDao()
    val metricDao = database.metricDao()

    fun getAllShops(): LiveData<List<String>> = shopProductDao.getAll().map { list ->
        list.map { shop ->
            shop.name
        }
    }

    fun getAllCategories(): LiveData<List<String>> = categoryProductDao.getAll().map { list ->
        list.map { category ->
            category.name
        }
    }

    fun getAllMetrics(): LiveData<List<String>> = metricDao.getAll().map { list ->
        list.map { metric ->
            metric.name
        }
    }

    companion object {
        private var nRepository: DictionaryProductRepository? = null
        fun getInstance(database: AppDatabase): DictionaryProductRepository {
            if (nRepository == null) nRepository = DictionaryProductRepository(database)
            return nRepository!!
        }
    }
}