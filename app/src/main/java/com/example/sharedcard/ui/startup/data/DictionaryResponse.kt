package com.example.sharedcard.ui.startup.data

import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.recipe.RecipeEntity
import com.example.sharedcard.database.entity.recipe_product.RecipeProductEntity
import com.example.sharedcard.database.entity.shop.ShopEntity

data class DictionaryResponse(
    val categories: List<CategoryProductEntity>,
    val currencies: List<CurrencyEntity>,
    val metrics: List<MetricEntity>,
    val products: List<ProductEntity>,
    val recipes: List<RecipeEntity>,
    val recipeProducts: List<RecipeProductEntity>,
    val shops: List<ShopEntity>
)