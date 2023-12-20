package com.example.sharedcard.database.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency",
    primaryKeys = arrayOf("id")
)
data class CurrencyEntity(
    val id: Long,
    val name: String
)
