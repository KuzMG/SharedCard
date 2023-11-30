package com.example.sharedcard.retrofit.api

import com.project.shared_card.database.dao.product.ProductEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {
    @GET("/product/get_all_active/{id}")
    fun getAllProduct(@Path("id") id: Long): Call<List<ProductEntity>>

    @POST("/product/save_product")
    fun save(@Body product: ProductEntity): Call<Int>

    @PUT("/product/update_product")
    fun updateProduct(@Body product: ProductEntity): Call<ProductEntity>

    @PUT("/product/delete_product")
    fun delete(@Body product: ProductEntity): Call<ProductEntity>
}