package com.example.sharedcard.database.entity.target

import androidx.room.ColumnInfo
import java.util.Date
import java.util.UUID

data class TargetHistory(
    val id: UUID,
    val name: String,
    val category: String,
    @ColumnInfo("first_price")
    val priceFirst: Int,
    private val currencyFirst: String,
    @ColumnInfo("last_price")
    val priceLast: Int,
    private val currencyLast: String,
    val user: String,
    @ColumnInfo("id_user")
    val userId: UUID,
    val shop: String,
    private val dateLast: Long
) {
    val priceListFirst: String
        get() = "$priceFirst $currencyFirst"

    val priceListLast: String
        get() = "$priceLast $currencyLast"
    val date: Date
        get() = Date(dateLast)
}
