package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.shop.ShopEntity
import java.util.Currency

class DictionaryRepository private constructor(database: AppDatabase) {
    private val shopDao = database.shopDao()
    private val categoryDao = database.categoryDao()
    private val metricDao = database.metricDao()
    private val currencyDao= database.currencyDao()
    private val productDao = database.productDao()
    fun addMetrics(metrics: List<MetricEntity>){
        metricDao.add(metrics)
    }

    fun addShops(shops: List<ShopEntity>){
        shopDao.add(shops)
    }
    fun addProducts(products: List<ProductEntity>){
        productDao.add(products)
    }
    fun addCurrencies(currencies: List<CurrencyEntity>){
        currencyDao.add(currencies)
    }
    fun addCategories(categories: List<CategoryEntity>){
        categoryDao.add(categories)
    }
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