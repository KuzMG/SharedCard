package com.example.sharedcard.database.entity.recipe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(recipe :List<RecipeEntity>)
    @Query("select * from recipe where id_category=:idCategory")
    fun getByIdCategory(idCategory: Int) : LiveData<List<Recipe>>
    @Query("select * from recipe join recipe_product on recipe.id = recipe_product.id_recipe where id=:idRecipe")
    fun getById(idRecipe: Int): LiveData<RecipeWithProducts>
}