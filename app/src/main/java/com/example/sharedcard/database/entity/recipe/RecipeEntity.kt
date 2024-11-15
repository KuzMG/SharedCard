package com.example.sharedcard.database.entity.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe",
    primaryKeys = ["id"])
data class RecipeEntity(
    val id: Int,
    val name:String,
    val name_en: String,
    val description: String,
    val portion: Int,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val pic: String,
    @ColumnInfo(name = "id_category")
    @SerializedName("id_category")
    val idCategory: Int
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}