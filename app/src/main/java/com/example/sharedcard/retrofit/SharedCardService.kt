package com.example.sharedcard.retrofit

import android.content.Context
import androidx.room.Room
import com.example.sharedcard.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SharedCardService {
    companion object {
        private var nRetrofit: Retrofit? = null
        fun getInstance(): Retrofit {
            if (nRetrofit == null) {
                nRetrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.80.31:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return nRetrofit!!
        }
    }
}