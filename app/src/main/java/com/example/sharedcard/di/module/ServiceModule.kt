package com.example.sharedcard.di.module

import com.example.sharedcard.retrofit.api.GroupApi
import com.example.sharedcard.retrofit.api.UserApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class ServiceModule {
    companion object{
        const val URL = "http://192.168.0.104:8000"
    }
    @Singleton
    @Provides
    fun provideGroupApi() = Retrofit
        .Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<GroupApi>()

    @Singleton
    @Provides
    fun provideUserApi() = Retrofit
        .Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<UserApi>()
}