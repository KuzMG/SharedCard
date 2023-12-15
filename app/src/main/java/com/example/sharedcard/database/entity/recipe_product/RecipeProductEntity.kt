package com.example.sharedcard.database.entity.recipe_product

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "recipe_product",
    primaryKeys = arrayOf("id_product", "id_recipe")
)
data class RecipeProductEntity(
    @ColumnInfo("id_product")
    val idProduct: Long,
    @ColumnInfo("id_recipe")
    val idRecipe: Long,
    @ColumnInfo("id_metric")
    val idMetric: Long,
    val count: Int
)