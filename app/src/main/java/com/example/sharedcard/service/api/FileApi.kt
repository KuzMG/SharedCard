package com.example.sharedcard.service.api

import com.example.sharedcard.service.dto.FileResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface FileApi {
    @POST("/pic/group")
    fun saveGroupPic(@HeaderMap header: Map<String,String>,@Body body: FileResponse): Call<ResponseBody>

    @POST("/pic/person")
    fun savePersonPic(@HeaderMap header: Map<String,String>, @Body body: FileResponse): Call<ResponseBody>
}