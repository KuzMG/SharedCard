package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.currency.CurrencyEntity
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApi {
    @GET("/currency/get_all")
    fun allCurrencies(): Call<List<CurrencyEntity>>
}