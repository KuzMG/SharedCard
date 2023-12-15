package com.example.sharedcard.database.entity.shop

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop")
class ShopEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)