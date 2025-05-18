package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sharedcard.database.entity.category_product.CategoryProductDao
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.database.entity.currency.CurrencyDao
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricDao
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.database.entity.product.ProductDao
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.Recipe
import com.example.sharedcard.database.entity.recipe.RecipeDao
import com.example.sharedcard.database.entity.recipe.RecipeWithProducts
import com.example.sharedcard.database.entity.recipe_product.RecipeProductDao
import com.example.sharedcard.database.entity.shop.ShopDao
import com.example.sharedcard.database.entity.shop.ShopEntity
import com.example.sharedcard.ui.startup.data.DictionaryResponse
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val shopDao: ShopDao,
    private val categoryProductDao: CategoryProductDao,
    private val metricDao: MetricDao,
    private val currencyDao: CurrencyDao,
    private val productDao: ProductDao,
    private val recipeDao: RecipeDao,
    private val recipeProductDao: RecipeProductDao,
    private val queryPreferences: QueryPreferences
) {

    fun getAllShops(): LiveData<List<ShopEntity>> = shopDao.getAll()
    fun getAllCategoriesProduct(): LiveData<List<CategoryProductEntity>> =
        categoryProductDao.getAllProduct()

    fun getAllCategoriesRecipe(): LiveData<List<CategoryProductEntity>> = MutableLiveData()



    fun getAllCurrency()  = currencyDao.getAll()

    fun getProductByCategory(id: Int) = productDao.getAllByCategory(id)

    fun getProductByQuery(query: String) = productDao.getAllByQuery("%$query%")
    fun getProductPopular() = productDao.getPopular()

    fun synchronization(response: DictionaryResponse) {
        categoryProductDao.add(response.categories)
        metricDao.add(response.metrics)
        currencyDao.add(response.currencies)
        shopDao.add(response.shops)
        productDao.add(response.products)
        recipeDao.add(response.recipes)
        recipeProductDao.add(response.recipeProducts)
    }

    fun getCategoryProductLiveData(idCategory: Int): LiveData<CategoryProductEntity> =
        categoryProductDao.getByIdLiveData(idCategory)

    fun getCategoryProduct(idCategory: Int) =
        categoryProductDao.getById(idCategory)

    fun getRecipesByCategory(idCategory: Int): LiveData<List<Recipe>> = MutableLiveData()
    fun getRecipe(idRecipe: Int): LiveData<RecipeWithProducts> = MutableLiveData()
    fun getMetricById(id: Int): MetricEntity = metricDao.getById(id)
    fun getCurrency() = currencyDao.getLiveData(queryPreferences.currency)
    fun getShopById(shopId: Int) = shopDao.get(shopId)
    fun getCurrencyId() = queryPreferences.currency
    fun setCurrency(currencyId: Int) {
        queryPreferences.currency =currencyId
    }

    fun getCurrencyByBasketId(basketId: UUID) = currencyDao.getByBasketId(basketId)
    fun updateProductWeight(productId: Int,weight: Float) {
        productDao.updateWeight(productId,weight)
    }

    fun getAllProduct() = productDao.getAll()
    fun updateCategoryWeight(categoryId: Int, weight: Float) {
        categoryProductDao.updateWeight(categoryId,weight)
    }

    fun getProductById(idProduct: Int)  = productDao.getById(idProduct)
    fun getCurrencyById(idCurrency: Int) = currencyDao.get(idCurrency)

}