package com.example.sharedcard.retrofit.api

import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupUsersApi {
    @POST("/group_users/get_all/{id}")
    fun getAllUsersInGroup(@Path("id") id: Long): Call<List<GroupUsersEntity>>

    @POST("/group_users/save_user")
    fun saveUserInGroup(@Body groupUsers: GroupUsersEntity): Call<Boolean>

    @PUT("/group_users/delete_user/{id}")
    fun deleteUser(@Body groupUsers: GroupUsersEntity): Call<*>

}