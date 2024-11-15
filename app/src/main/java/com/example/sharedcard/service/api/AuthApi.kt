package com.example.sharedcard.service.api

import com.example.sharedcard.service.dto.AuthResponse
import com.example.sharedcard.service.dto.AccountResponse
import com.example.sharedcard.service.dto.RegistrationBody
import com.example.sharedcard.ui.startup.data.DictionaryResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    companion object{
        const val HEADER_ID_USER = "id-user"
        const val HEADER_PASSWORD_USER = "password-user"
    }
    @GET("/authentication")
    fun authorization(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<AuthResponse>

    @POST("/registration")
    fun registration(@Body body: RegistrationBody): Call<ResponseBody>

    @GET("/verification")
    fun verification(
        @Query("login") login: String,
        @Query("password") password: String,
        @Query("code") code: String
    ): Call<AuthResponse>

    @GET("/dictionary")
    fun getDictionary(@HeaderMap headers: Map<String,String>): Call<DictionaryResponse>
    @GET("/account")
    fun getAccount(@HeaderMap headers: Map<String,String>): Call<AccountResponse>
}