package com.example.sharedcard.database.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency",
    primaryKeys = ["id"]
)
data class CurrencyEntity(
    val id: Int,
    val name: String,
    val name_en: String,
    val symbol: String
)
