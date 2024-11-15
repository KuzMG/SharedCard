package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("select * from product " +
            "where id_category=:id_category")
    fun getAllByCategory(id_category: Int): LiveData<List<ProductEntity>>

    @Query("select * from product " +
            "where name like :query limit 5")
    fun getAllByQuery(query: String): LiveData<List<ProductEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(products :List<ProductEntity>)
}