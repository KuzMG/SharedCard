package com.example.sharedcard.database.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency"
)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val name_en: String,
    val symbol: String
)
