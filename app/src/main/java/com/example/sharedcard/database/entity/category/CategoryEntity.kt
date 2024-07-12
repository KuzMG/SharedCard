package com.example.sharedcard.database.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category",
    primaryKeys = arrayOf("id")
)
class CategoryEntity(
    val id: Long,
    val name: String,
    val name_en: String,
    val type: Int
)