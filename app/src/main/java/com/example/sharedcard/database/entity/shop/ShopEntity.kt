package com.example.sharedcard.database.entity.shop

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "shop",
    primaryKeys = ["id"]
)
class ShopEntity(
    val id: Int,
    val name: String,
    val name_en: String,
    val status: Boolean

)