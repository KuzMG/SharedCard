package com.example.sharedcard.database.entity.shop

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "shop",
    primaryKeys = ["id"]
)
class ShopEntity(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String

)