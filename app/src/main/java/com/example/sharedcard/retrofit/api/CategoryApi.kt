package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.category.CategoryEntity
import retrofit2.Call
import retrofit2.http.GET

interface CategoryApi {
    @GET("/category/product/get_all")
    fun allCategoryProduct(): Call<List<CategoryEntity>>
}