package com.example.sharedcard.database.entity.recipe

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.metric.MetricEntity
import com.example.sharedcard.database.entity.product.ProductEntity

data class RecipeWithProducts(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_recipe",
        entity = RecipeProduct::class
    )
    val products: List<RecipeProduct>
)

data class RecipeProduct(
    @ColumnInfo("id_product")
    private val idProduct: Int,
    @ColumnInfo("id_metric")
    private val idMetric: Int,
    private val count: Int,
    private val weight: Int,
    @Relation(
        parentColumn = "id_metric",
        entityColumn = "id"
    )
    val metric: MetricEntity,
    @Relation(
        parentColumn = "id_product",
        entityColumn = "id"
    )
    val product: ProductEntity
)