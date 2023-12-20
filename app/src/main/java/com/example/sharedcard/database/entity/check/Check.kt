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
    val idUser: Long,
    val description: String,
    @ColumnInfo("date_first")
    private val dateFirst: Long,
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
        get() = Date(dateFirst)
}
