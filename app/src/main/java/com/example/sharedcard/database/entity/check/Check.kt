package com.example.sharedcard.database.entity.check

import androidx.room.ColumnInfo
import com.example.sharedcard.di.module.ServiceModule
import java.util.Date
import java.util.UUID

data class Check(
    val id: UUID,
    val name: String,
    val status: Int,
    val category: String,
    private val count: Int,
    private val metric: String,
    val user: String,
    val picUser: String,
    val description: String,
    @ColumnInfo("date_first")
    private val dateFirst: Long,
    val calories: Int?,
    val fat: Double?,
    val carb: Double?,
    val protein: Double?
) {
    val userPic: String
        get() = ServiceModule.URL_REST + "/$picUser"
    val quantity: String
        get() = "$count $metric"
    val pfc: String
        get() = "$calories/$protein/$fat/$carb"
    val date: Date
        get() = Date(dateFirst)
}
