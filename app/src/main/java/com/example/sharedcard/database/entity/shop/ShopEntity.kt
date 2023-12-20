package com.example.sharedcard.database.entity.shop

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "shop",
    primaryKeys = arrayOf("id")
)
class ShopEntity(
    val id: Long,
    val name: String,
    val name_en: String,
    val status: Boolean

)