package com.example.sharedcard.database.entity.product

import android.text.format.DateFormat
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.Date

data class Product(
    val id: Long,
    val name: String,
    val status: Int,
    val category: String,
    private val count: Int,
    private val metric: String,
    val creator: String,
    private val dateFirst: Long
) {
    val quantity: String
        get() = "$count $metric"
    val date: Date
        get() = Date(dateFirst)
}
