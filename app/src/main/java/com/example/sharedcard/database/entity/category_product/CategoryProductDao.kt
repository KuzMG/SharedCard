package com.example.sharedcard.database.entity.category_product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: List<CategoryProductEntity>)

    @Query("Select * from category_product order by weight desc")
    fun getAllProduct(): LiveData<List<CategoryProductEntity>>

    @Query("Select * from category_product where id = :categoryId")
    fun getById(categoryId: Int): CategoryProductEntity

    @Query("Select * from category_product where id = :categoryId")
    fun getByIdLiveData(categoryId: Int): LiveData<CategoryProductEntity>
    @Query("update category_product set weight = :weight where id=:categoryId")
    fun updateWeight(categoryId: Int, weight: Float)
}