package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.UUID

@Dao
interface ProductDao {
    @Query("select * from product " +
            "where id_category_product=:categoryId order by popularity desc"
    )
    fun getAllByCategory(categoryId: Int): LiveData<List<Product>>

    @Query("select * from product " +
            "where name like :query  order by popularity desc limit 4"
    )
    fun getAllByQuery(query: String): LiveData<List<Product>>
    @Query("select * from product order by popularity desc limit 4 ")
    fun getPopular():  LiveData<List<Product>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(products :List<ProductEntity>)

    @Query("select * from product")
    fun getAll() : List<ProductEntity>
    @Query("update product set popularity = :weight where id=:productId")
    fun updateWeight(productId: Int, weight: Float)
    @Query("select * from product where id=:productId")
    fun getById(productId: Int): ProductEntity


}