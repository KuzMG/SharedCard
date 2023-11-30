package com.example.sharedcard.retrofit.api

import com.project.shared_card.database.dao.target.TargetEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TargetApi {
    @GET("/target/get_all_active/{id}")
    fun getAllTarget(@Path("id") id: Long): Call<List<TargetEntity>>

    @POST("/target/save_target")
    fun save(@Body target: TargetEntity): Call<Int>

    @PUT("/target/update_target")
    fun updateTarget(@Body target: TargetEntity): Call<TargetEntity>

    @PUT("/target/delete_target")
    fun delete(@Body target: TargetEntity): Call<TargetEntity>
}