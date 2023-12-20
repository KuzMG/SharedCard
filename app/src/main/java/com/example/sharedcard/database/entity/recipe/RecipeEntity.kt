package com.example.sharedcard.database.entity.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name:String,
    val name_en: String,
    val description: String,
    val portion: Int,
    val calorie: Int
)