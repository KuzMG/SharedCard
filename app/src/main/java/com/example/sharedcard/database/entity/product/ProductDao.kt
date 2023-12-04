package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("select product.id,product.name,status,c.name as category,count,m.name as metric,u.name as creator,date_first as dateFirst from product " +
            "join category_product as c on id_category = c.id " +
            "join metric as m on id_metric = m.id " +
            "left join user as u on id_user_creator = u.id " +
            "where product.id_group = :id and status != 2 " +
            "order by status, date_first desc")
    fun getAllForCheck(id: Long): LiveData<List<Product>>

    @Query("select product.id,product.name,status,c.name as category,count,m.name as metric,u.name as creator,date_first as dateFirst from product " +
            "join category_product as c on id_category = c.id " +
            "join metric as m on id_metric = m.id " +
            "left join user as u on id_user_creator = u.id  " +
            "where product.id_group = :id and status != 2 and product.name like :query " +
            "order by status,date_first desc")
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Product>>

    @Query("select * from product where id_group = :id and status = 2")
    fun getAllForHistory(id: Long): LiveData<List<ProductEntity>>


    @Insert
    fun add(product: ProductEntity)

    @Query("update product set status = :status where id=:id")
    fun uprateStatus(id: Long, status: Int)

    @Update
    fun update(product: ProductEntity)

    @Delete
    fun delete(product: ProductEntity)
}