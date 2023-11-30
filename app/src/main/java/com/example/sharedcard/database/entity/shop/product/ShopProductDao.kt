package com.example.sharedcard.database.entity.shop.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.shop_product.ShopProductEntity

@Dao
interface ShopProductDao {
    @Insert
    fun add(entity: List<ShopProductEntity>)

    @Query("select * from shop_product")
    fun getAll(): LiveData<List<ShopProductEntity>>
}