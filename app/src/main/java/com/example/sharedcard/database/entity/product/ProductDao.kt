package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("select * from product " +
            "where id_category=:id_category")
    fun getAllById(id_category: Long): LiveData<List<ProductEntity>>

    @Query("select * from product " +
            "where name like :query limit 5")
    fun getAllByQuery(query: String): LiveData<List<ProductEntity>>
    @Insert
    fun add(products :List<ProductEntity>)
}