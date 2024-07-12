package com.example.sharedcard.database.entity.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name:String,
    val name_en: String,
    val description: String,
    val portion: Int,
    val calorie: Int,
    @ColumnInfo(name = "id_category")
    @SerializedName("id_category")
    val idCategory: Long
)