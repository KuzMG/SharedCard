package com.example.sharedcard.database.entity.recipe_product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sharedcard.database.entity.recipe.RecipeEntity

@Dao
interface RecipeProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(recipeProduct :List<RecipeProductEntity>)
    @Query("select * from recipe_product")
    fun getAll(): LiveData<List<RecipeProductEntity>>
}