package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("select * from product " +
            "where id_category=:id")
    fun getAllById(id: Long): LiveData<List<ProductEntity>>

    @Query("select * from product " +
            "where name like :query")
    fun getAllByQuery(query: String): LiveData<List<ProductEntity>>
    @Insert
    fun add(products :List<ProductEntity>)
}