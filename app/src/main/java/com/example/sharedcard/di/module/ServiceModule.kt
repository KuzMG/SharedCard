package com.example.sharedcard.di.module

import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.api.FileApi
import com.example.sharedcard.service.api.GroupApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import javax.inject.Singleton

@Module
class ServiceModule {
    companion object {
        private const val IP = "192.168.0.107:8080"
        const val URL_REST = "http://$IP"
        private const val URL_STOMP = "ws://$IP/stomp"
    }

    @Singleton
    @Provides
    fun provideAuthApi() = Retrofit
        .Builder()
        .baseUrl(URL_REST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideGroupApi() = Retrofit
        .Builder()
        .baseUrl(URL_REST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GroupApi::class.java)

    @Singleton
    @Provides
    fun provideFileApi() = Retrofit
        .Builder()
        .baseUrl(URL_REST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FileApi::class.java)

    @Singleton
    @Provides
    fun provideStomp() = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL_STOMP)

}


