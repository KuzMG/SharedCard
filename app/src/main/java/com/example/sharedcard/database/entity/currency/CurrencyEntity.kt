package com.example.sharedcard.database.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "currency",
    primaryKeys = ["id"]
)
data class CurrencyEntity(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String,
    val symbol: String
)
