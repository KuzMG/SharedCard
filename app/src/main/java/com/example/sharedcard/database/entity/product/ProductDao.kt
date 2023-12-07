package com.example.sharedcard.database.entity.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query(
        "select product.id,product.name,status,c.name as category,count,m.name as metric,u.name as creator,date_first as dateFirst from product " +
                "join category_product as c on id_category = c.id " +
                "join metric as m on id_metric = m.id " +
                "left join user as u on id_user_creator = u.id_user " +
                "where product.id_group = :id and status != 2 " +
                "order by status, date_first desc"
    )
    fun getAllForCheck(id: Long): LiveData<List<Product>>

    @Query(
        "select product.id,product.name,status,c.name as category,count,m.name as metric,u.name as creator,date_first as dateFirst from product " +
                "join category_product as c on id_category = c.id " +
                "join metric as m on id_metric = m.id " +
                "left join user as u on id_user_creator = u.id_user  " +
                "where product.id_group = :id and status != 2 and product.name like :query " +
                "order by status,date_first desc"
    )
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Product>>


    @Query("delete from product where id = :id")
    fun delete(id: Long)

    @Insert
    fun add(product: ProductEntity)

    @Query("update product set status = :status where id=:id")
    fun uprateStatus(id: Long, status: Int)

    @Query(
        "update product set " +
                "status = 2, " +
                "id_shop = :idShop, " +
                "price = :price, " +
                "id_currency = :currency, " +
                "id_user_buyer = :idUser, " +
                "date_last = :dataLast " +
                "where id=:id"
    )
    fun toHistory(
        id: Long,
        idShop: Long,
        price: Int,
        currency: Long,
        idUser: Long?,
        dataLast: Long
    )

}