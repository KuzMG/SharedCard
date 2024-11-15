package com.example.sharedcard.database.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sharedcard.di.module.ServiceModule

@Entity(
    tableName = "category",
    primaryKeys = arrayOf("id")
)
class CategoryEntity(
    val id: Int,
    val name: String,
    val name_en: String,
    val type: Int,
    val pic: String,
    val color: String,
    val popularity: Int = 0
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}