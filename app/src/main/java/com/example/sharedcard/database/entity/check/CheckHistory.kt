package com.example.sharedcard.database.entity.check

import androidx.room.ColumnInfo
import java.util.Date
import java.util.UUID

data class CheckHistory(
    @ColumnInfo("id_check")
    val id: UUID,
    val name: String,
    val category: String,
    val shop: String,
    val price: Int,
    private val currency: String,
    private val count: Int,
    private val metric: String,
    val userFirst: String,
    val idUserFirst: UUID,
    val userLast: String,
    val idUserLast: UUID,
    @ColumnInfo("date_last")
    private val dateLast: Long,
    val calorie: Int?,
    val fat: Double?,
    val carb: Double?,
    val protein: Double?
) {
    val quantity: String
        get() = "$count $metric"
    val pfc: String
        get() = "$calorie/$protein/$fat/$carb"
    val date: Date
        get() = Date(dateLast)
    val priceList: String
        get() = "$price $currency"
}
