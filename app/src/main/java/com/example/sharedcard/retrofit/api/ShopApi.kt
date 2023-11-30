package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.shop.product.ShopProductDao
import com.project.shared_card.database.dao.shop_target.ShopTargetEntity
import retrofit2.Call
import retrofit2.http.GET

interface ShopApi {
    @GET("/shop/target/get_all")
    fun allShopTarget(): Call<List<ShopTargetEntity>>

    @GET("/shop/product/get_all")
    fun allShopProduct(): Call<List<ShopProductDao>>

}