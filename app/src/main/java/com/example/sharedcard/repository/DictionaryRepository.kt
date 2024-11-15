package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.entity.category.CategoryDao
import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.Recipe
import com.example.sharedcard.database.entity.recipe.RecipeDao
import com.example.sharedcard.database.entity.recipe_product.RecipeProductDao
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.database.entity.shop.ShopEntity
import com.example.sharedcard.ui.startup.data.DictionaryResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val shopDao: ShopDao,
    private val categoryDao: CategoryDao,
    private val metricDao: MetricDao,
    private val currencyDao: CurrencyDao,
    private val productDao: ProductDao,
    private val recipeDao: RecipeDao,
    private val recipeProductDao: RecipeProductDao
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
    fun getAllCategoriesRecipe(): LiveData<List<CategoryEntity>> = categoryDao.getAllRecipe()
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

    fun getProductByCategory(id: Int) = productDao.getAllByCategory(id)

    fun getProductByQuery(query: String) = productDao.getAllByQuery("%$query%")

    fun synchronization(response: DictionaryResponse) {
        categoryDao.add(response.categories)
        metricDao.add(response.metrics)
        currencyDao.add(response.currencies)
        shopDao.add(response.shops)
        productDao.add(response.products)
        recipeDao.add(response.recipes)
        recipeProductDao.add(response.recipeProducts)
    }

    fun getCategory(idCategory: Int): LiveData<CategoryEntity> = categoryDao.getById(idCategory)
    fun getRecipesByCategory(idCategory: Int): LiveData<List<Recipe>>  = recipeDao.getById(idCategory)


}