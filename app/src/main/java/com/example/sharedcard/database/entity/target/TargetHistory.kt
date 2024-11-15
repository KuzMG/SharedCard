package com.example.sharedcard.database.entity.target

import androidx.room.ColumnInfo
import com.example.sharedcard.di.module.ServiceModule
import java.util.Date
import java.util.UUID

data class TargetHistory(
    val id: UUID,
    val name: String,
    val category: String,
    @ColumnInfo("price_first")
    val priceFirst: Int,
    private val currencyFirst: String,
    @ColumnInfo("price_last")
    val priceLast: Int,
    private val currencyLast: String,
    val userFirst: String,
    val picUserFirst: String,
    val userLast: String,
    val picUserLast: String,
    val shop: String,
    private val dateLast: Long
) {
    val userFirstPic: String
        get() = ServiceModule.URL_REST + "/$picUserFirst"
    val userLastPic: String
        get() = ServiceModule.URL_REST + "/$picUserLast"
    val priceListFirst: String
        get() = "$priceFirst $currencyFirst"

    val priceListLast: String
        get() = "$priceLast $currencyLast"
    val date: Date
        get() = Date(dateLast)
}
