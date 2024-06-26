package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {
    @GET("/product/get_all_active/{id}")
    fun getAllProduct(@Path("id") id: Long): Call<List<ProductEntity>>

}