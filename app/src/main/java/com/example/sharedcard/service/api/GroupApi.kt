package com.example.sharedcard.service.api

import com.example.sharedcard.service.dto.CreateGroupResponse
import com.example.sharedcard.service.dto.JoinInGroupResponse
import com.example.sharedcard.service.dto.TokenResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface GroupApi {
    @POST("/server/group/join")
    fun joinGroupSync(@Body body: JoinInGroupResponse): Call<ResponseBody>

    @GET("/server/group/token")
    fun getToken(
        @Query("group-id") groupId: UUID,
        @Query("user-id") userId: UUID,
        @Query("password") password: String
    ): Call<TokenResponse>

    @POST("/server/group/create")
    fun createGroup(
        @Query("user-id") userId: UUID,
        @Query("password") password: String,
        @Body body: CreateGroupResponse
    ): Call<ResponseBody>
}