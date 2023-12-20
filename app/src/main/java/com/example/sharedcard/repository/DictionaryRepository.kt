package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.shop.ShopEntity

class DictionaryRepository private constructor(database: AppDatabase) {
    private val shopDao = database.shopDao()
    private val categoryDao = database.categoryDao()
    private val metricDao = database.metricDao()
    private val currencyDao= database.currencyDao()
    private val productDao = database.productDao()

    fun getAllShopsProduct(): LiveData<List<ShopEntity>> = shopDao.getAllProduct()
    fun getAllShopsTarget(): LiveData<List<ShopEntity>> = shopDao.getAllTarget()
    fun getAllCategoriesProduct(): LiveData<List<CategoryEntity>> = categoryDao.getAllProduct()
    fun getAllCategoriesTarget(): LiveData<List<CategoryEntity>> = categoryDao.getAllTarget()

    fun getAllMetrics(): LiveData<List<String>> = metricDao.getAll().map { list ->
        list.map { metric ->
            metric.name
        }
    }


    fun getAllCurrency(): LiveData<List<String>> = currencyDao.getAll().map { list ->
        list.map { currency ->
            currency.name
        }
    }

    fun getProductById(id: Long) = productDao.getAllById(id)

    fun getProductByQuery(query: String) = productDao.getAllByQuery("%$query%")

    companion object {
        private var nRepository: DictionaryRepository? = null
        fun getInstance(database: AppDatabase): DictionaryRepository {
            if (nRepository == null) nRepository = DictionaryRepository(database)
            return nRepository!!
        }
    }
}