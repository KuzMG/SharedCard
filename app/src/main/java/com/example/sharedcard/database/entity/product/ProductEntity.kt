package com.example.sharedcard.database.entity.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product",
    primaryKeys = ["id"])
data class ProductEntity(
    val id: Int,
    val name: String,
    val name_en: String,
    val fat: Double?,
    val protein: Double?,
    val carb: Double?,
    val calories: Double?,
    val weight: Int,
    @ColumnInfo(name = "id_category")
    @SerializedName("id_category")
    val idCategory: Int,
    val allergy: Boolean,
    val pic: String
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}