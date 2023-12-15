package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.shop.ShopDao
import retrofit2.Call
import retrofit2.http.GET

interface ShopApi {

    @GET("/shop/product/get_all")
    fun allShopProduct(): Call<List<ShopDao>>

}