package com.example.sharedcard.database.entity.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: List<CategoryEntity>)

    @Query("Select * from category where type = 1 order by popularity desc")
    fun getAllProduct(): LiveData<List<CategoryEntity>>

    @Query("Select * from category where type = 2 order by popularity desc")
    fun getAllTarget(): LiveData<List<CategoryEntity>>

    @Query("Select * from category where type = 3 order by popularity desc")
    fun getAllRecipe(): LiveData<List<CategoryEntity>>
    @Query("Select * from category where id = :idCategory")
    fun getById(idCategory: Int): LiveData<CategoryEntity>
}