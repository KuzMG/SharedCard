package com.example.sharedcard.database.entity.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    fun add(entity: List<CategoryEntity>)

    @Query("Select * from category")
    fun getAll(): LiveData<List<CategoryEntity>>
}