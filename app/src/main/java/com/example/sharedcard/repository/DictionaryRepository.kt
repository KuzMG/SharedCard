package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.category.CategoryDao
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.database.entity.shop.ShopEntity
import java.util.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val shopDao: ShopDao,
    private val categoryDao: CategoryDao,
    private val metricDao: MetricDao,
    private val currencyDao: CurrencyDao,
    private val productDao: ProductDao
) {

    fun addMetrics(metrics: List<MetricEntity>) {
        metricDao.add(metrics)
    }

    fun addShops(shops: List<ShopEntity>) {
        shopDao.add(shops)
    }

    fun addProducts(products: List<ProductEntity>) {
        productDao.add(products)
    }

    fun addCurrencies(currencies: List<CurrencyEntity>) {
        currencyDao.add(currencies)
    }

    fun addCategories(categories: List<CategoryEntity>) {
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


}