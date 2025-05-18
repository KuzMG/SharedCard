package com.example.sharedcard.database.entity.category_product

import androidx.room.Entity
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "category_product",
    primaryKeys = ["id"]
)
class CategoryProductEntity(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String,
    val pic: String,
    val weight: Float = 0F
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}