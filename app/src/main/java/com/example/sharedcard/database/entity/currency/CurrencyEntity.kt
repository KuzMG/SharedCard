package com.example.sharedcard.database.entity.currency

import androidx.room.Entity

@Entity(
    tableName = "currency",
    primaryKeys = arrayOf("code")
)
data class CurrencyEntity(
    val code: String,
    val name: String
)
