package com.example.sharedcard.retrofit.api

import com.project.shared_card.database.dao.categories_target.CategoryTargetEntity
import com.project.shared_card.database.dao.category_product.CategoryProductEntity
import retrofit2.Call
import retrofit2.http.GET

interface CategoryApi {
    @GET("/category/product/get_all")
    fun allCategoryProduct(): Call<List<CategoryProductEntity>>

    @GET("/category/target/get_all")
    fun allCategoryTarget(): Call<List<CategoryTargetEntity>>
}