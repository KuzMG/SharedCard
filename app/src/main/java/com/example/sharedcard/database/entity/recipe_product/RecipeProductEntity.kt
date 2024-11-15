package com.example.sharedcard.database.entity.recipe_product

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "recipe_product",
    primaryKeys = ["id_product", "id_recipe"]
)
data class RecipeProductEntity(
    @ColumnInfo("id_product")
    val idProduct: Int,
    @ColumnInfo("id_recipe")
    val idRecipe: Int,
    @ColumnInfo("id_metric")
    val idMetric: Int,
    val count: Int
)