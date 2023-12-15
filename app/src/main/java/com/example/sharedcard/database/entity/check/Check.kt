package com.example.sharedcard.database.entity.check

import androidx.room.ColumnInfo
import java.util.Date

data class Check(
    @ColumnInfo("id_check")
    val id: Long,
    val name: String,
    val status: Int,
    val category: String,
    private val count: Int,
    private val metric: String,
    val user: String,
    @ColumnInfo("date_first")
    private val dateFirst: Long,
    val calory: Int?,
    val fat: Int?,
    val carb: Int?,
    val protein: Int?
) {
    val quantity: String
        get() = "$count $metric"
    val date: Date
        get() = Date(dateFirst)
}
