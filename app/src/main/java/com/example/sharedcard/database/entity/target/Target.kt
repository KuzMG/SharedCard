package com.example.sharedcard.database.entity.target

import androidx.room.ColumnInfo
import com.example.sharedcard.di.module.ServiceModule
import java.util.Date
import java.util.UUID

data class Target(
    val id: UUID,
    val name: String,
    val category: String,
    val price: Int,
    private val currency: String,
    val user: String,
    val picUser: String,
    private val dateFirst: Long
) {
    val userPic: String
        get() = ServiceModule.URL_REST  + "/$picUser"
    val priceList: String
        get() = "$price $currency"
    val date: Date
        get() = Date(dateFirst)
}
