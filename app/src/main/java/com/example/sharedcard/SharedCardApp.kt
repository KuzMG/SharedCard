package com.example.sharedcard

import android.app.Application
import com.example.sharedcard.di.AppComponent
import com.example.sharedcard.di.DaggerAppComponent
import com.example.sharedcard.notification.NotificationHelper


class SharedCardApp : Application() {


    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}