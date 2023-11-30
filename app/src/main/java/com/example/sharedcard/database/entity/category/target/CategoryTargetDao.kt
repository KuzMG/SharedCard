package com.example.sharedcard.database.entity.category.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.categories_target.CategoryTargetEntity

@Dao
interface CategoryTargetDao {
    @Insert
    fun add(entity: List<CategoryTargetEntity>)

    @Query("Select * from category_target")
    fun getAll(): LiveData<List<CategoryTargetEntity>>
}