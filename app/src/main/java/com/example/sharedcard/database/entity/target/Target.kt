package com.example.sharedcard.database.entity.target

import androidx.room.ColumnInfo
import java.util.Date
import java.util.UUID

data class Target(
    val id: UUID,
    val name: String,
    val category: String,
    val price: Int,
    private val currency: String,
    val user: String,
    @ColumnInfo("id_user")
    val userId: UUID,
    private val dateFirst: Long
) {
    val priceList: String
        get() = "$price $currency"
    val date: Date
        get() = Date(dateFirst)
}
