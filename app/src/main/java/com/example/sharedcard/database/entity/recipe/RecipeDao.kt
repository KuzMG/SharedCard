package com.example.sharedcard.database.entity.recipe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sharedcard.database.entity.product.ProductEntity

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(recipe :List<RecipeEntity>)
    @Query("select * from recipe where id_category=:idCategory")
    fun getById(idCategory: Int) : LiveData<List<Recipe>>
}