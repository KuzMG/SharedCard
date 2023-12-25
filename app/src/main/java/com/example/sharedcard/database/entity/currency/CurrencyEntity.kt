package com.example.sharedcard.database.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency"
)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val code: String,
    val name: String,
    val units: Int,
    val course: Double,
    val code_en: String,
)
