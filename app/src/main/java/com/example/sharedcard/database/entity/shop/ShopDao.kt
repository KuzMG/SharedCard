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

    @Query("select * from shop")
    fun getAll(): LiveData<List<ShopEntity>>
    @Query("select * from shop where id=:shopId")
    fun get(shopId: Int) : LiveData<ShopEntity>


}