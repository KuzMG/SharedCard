package com.example.sharedcard.database.entity.shop

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShopDao {
    @Insert
    fun add(entity: List<ShopEntity>)

    @Query("select * from shop")
    fun getAll(): LiveData<List<ShopEntity>>
}