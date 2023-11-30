package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.user.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST("/user/save")
    fun addUser(@Body user: UserEntity): Call<Long>

    @GET("/user/{id}")
    fun getUserById(@Path("id") id: Long): Call<UserEntity>

    @PUT("/user/update_name/{id}")
    fun updateName(@Path("id") id: Long,@Body name: String): Call<UserEntity>

    @PUT("/user/update_photo/{id}")
    fun updatePhoto(@Path("id") id: Long,@Body photo: ByteArray): Call<UserEntity>
}