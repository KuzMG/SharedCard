package com.example.sharedcard.database.entity.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product",
    primaryKeys = arrayOf("id"))
data class ProductEntity(
    val id: Long,
    val name: String,
    val name_en: String,
    val fat: Double?,
    val protein: Double?,
    val carb: Double?,
    val calorie: Double?,
    @ColumnInfo(name = "id_category")
    @SerializedName("id_category")
    val idCategory: Long
)