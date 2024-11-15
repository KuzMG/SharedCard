package com.example.sharedcard.database.entity.recipe

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: Int,
    val name:String,
    val name_en: String,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val pic: String,
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}