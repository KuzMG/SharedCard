package com.example.sharedcard.database.entity.category.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.category_product.CategoryProductEntity

@Dao
interface CategoryProductDao {
    @Insert
    fun add(entity: List<CategoryProductEntity>)

    @Query("Select * from category_product")
    fun getAll(): LiveData<List<CategoryProductEntity>>
}