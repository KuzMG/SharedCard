package com.example.sharedcard.database.entity.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val fat: Double?,
    val protein: Double?,
    val carb: Double?,
    val calorie: Int?,
    @ColumnInfo(name = "id_category")
    val idCategory: Long
)