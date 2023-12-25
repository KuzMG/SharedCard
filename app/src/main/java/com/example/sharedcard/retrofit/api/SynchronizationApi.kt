package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.category.CategoryEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.database.entity.shop.ShopEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SynchronizationApi {
    @GET("/products/")
    fun getProducts(): Call<List<ProductEntity>>
    @GET("/currencys/")
    fun getCurrencies(): Call<List<CurrencyEntity>>
    @GET("/shops/")
    fun getShops(): Call<List<ShopEntity>>

    @GET("/categories/")
    fun getCategories(): Call<List<CategoryEntity>>
    @GET("/metrics/")
    fun getMetrics(): Call<List<MetricEntity>>
}