package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApi {
    @POST("/group/save")
    fun addGroup(@Body group: GroupEntity): Call<Long>

    @GET("/group/{id}")
    fun getGroupById(@Path("id") id: Long): Call<GroupEntity>

    @PUT("/group/update_name")
    fun updateName(@Body name: String): Call<*>

    @PUT("/group/update_photo")
    fun updatePhoto(@Body photo: ByteArray): Call<*>
}