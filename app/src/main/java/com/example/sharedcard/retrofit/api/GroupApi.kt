package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface GroupApi {
    @POST("/group/save")
    fun addGroup(@Body group: GroupEntity): Call<Long>

    @GET("/group/{id}")
    fun getGroupById(@Path("id") id: Long): Call<GroupEntity>

    @PUT("/group/update_name")
    fun updateName(@Body name: String): Call<*>

    @POST("/groups/update_photo/{id}")
    fun updatePhoto(@Path("id") id: UUID, @Body photo: ByteArray)  : Call<Boolean>
}