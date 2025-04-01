package com.example.sharedcard.database.entity.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product",
    primaryKeys = ["id"])
data class ProductEntity(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String,
    val fat: Double?,
    val protein: Double?,
    val carb: Double?,
    val calories: Double?,
    val weight: Int,
    @ColumnInfo(name = "id_category_product")
    @SerializedName("id_category_product")
    val idCategory: Int,
    @ColumnInfo(name = "id_metric")
    @SerializedName("id_metric")
    val idMetric: Int,
    val allergy: Boolean,
    val pic: String,
    @ColumnInfo(name = "quantity_multiplier")
    @SerializedName("quantity_multiplier")
    val quantityMultiplier: Int
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}