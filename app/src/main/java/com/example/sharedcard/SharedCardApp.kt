package com.example.sharedcard

import android.app.Application
import com.example.sharedcard.di.AppComponent
import com.example.sharedcard.di.DaggerAppComponent


class SharedCardApp : Application() {


    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).build()
    }


}