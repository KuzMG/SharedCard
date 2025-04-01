package com.example.sharedcard.database.entity.product

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.sharedcard.database.entity.category_product.CategoryProductEntity
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName
import java.util.Locale.Category


data class Product(
    @Embedded
    val productEntity: ProductEntity,
    @Relation(parentColumn = "id_category_product", entityColumn = "id")
    val category: CategoryProductEntity,
    @Relation(parentColumn = "id_metric", entityColumn = "id")
    val metric: MetricEntity
)