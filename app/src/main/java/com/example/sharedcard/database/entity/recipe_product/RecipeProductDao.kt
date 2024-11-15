package com.example.sharedcard.database.entity.recipe_product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.sharedcard.database.entity.recipe.RecipeEntity

@Dao
interface RecipeProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(recipeProduct :List<RecipeProductEntity>)
}