package com.example.sharedcard.retrofit.api

import retrofit2.Call
import retrofit2.http.GET
import java.util.UUID

interface AuthApi {
    @GET("/auth/register")
    fun signUp(email: String,password: String): Call<UUID>
}