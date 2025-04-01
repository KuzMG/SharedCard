package com.example.sharedcard.util

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.di.AppComponent
import java.io.Serializable
import java.lang.Math.floor

val Context.appComponent: AppComponent
    get() = when (this) {
        is SharedCardApp -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
    get() = context!!.appComponent

fun Double.toStringFormat(): String {
    val value = kotlin.math.floor(this * 1000.0) / 1000.0
    return if (value.toString().split(".")[1].toInt() == 0) {
        this.toInt().toString()
    } else {
        value.toString()
    }
}


inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else ({
        getSerializable(key)
    }) as T?
}