package com.example.sharedcard.database.entity.shop.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.shop_target.ShopTargetEntity

@Dao
interface ShopTargetDao {
    @Insert
    fun add(entity: List<ShopTargetEntity>)

    @Query("select * from shop_target")
    fun getAll(): LiveData<List<ShopTargetEntity>>
}