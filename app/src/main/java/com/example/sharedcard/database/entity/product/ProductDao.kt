package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("select * from product " +
            "where id_category_product=:categoryId")
    fun getAllByCategory(categoryId: Int): LiveData<List<Product>>

    @Query("select * from product " +
            "where name like :query limit 4")
    fun getAllByQuery(query: String): LiveData<List<Product>>
    @Query("select * from product limit 4")
    fun getPopular():  LiveData<List<Product>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(products :List<ProductEntity>)

}