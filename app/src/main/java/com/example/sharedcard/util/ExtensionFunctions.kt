package com.example.sharedcard.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.di.AppComponent

val Context.appComponent: AppComponent
    get() = when (this) {
        is SharedCardApp -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent : AppComponent
    get() = context!!.appComponent


