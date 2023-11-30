package com.example.sharedcard.retrofit.api

import com.example.sharedcard.database.entity.metric.MetricEntity
import retrofit2.Call
import retrofit2.http.GET

interface MetricApi {
    @GET("/metric/get_all")
    fun allMetric(): Call<List<MetricEntity>>
}