package com.example.sharedcard.database.entity.shop

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: List<ShopEntity>)

    @Query("select * from shop where status = 1")
    fun getAllProduct(): LiveData<List<ShopEntity>>

    @Query("select * from shop where status = 0")
    fun getAllTarget(): LiveData<List<ShopEntity>>
}