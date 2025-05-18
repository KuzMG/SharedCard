package com.example.sharedcard.database.entity.basket

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.util.UUID

@Dao
interface BasketDao {
    @Query("select * from `basket` where id_purchase = :purchaseId order by (select id from history where history.id_basket = id)")
    fun getByPurchase(purchaseId: UUID): LiveData<List<Basket>>
    @Query("select sum(count) from `basket` where id_purchase = :purchaseId and (select id_basket from history where history.id_basket=basket.id) is null")
    fun getCount(purchaseId: UUID): LiveData<Double>

    @Query("delete from `basket` where id = :id")
    fun delete(id: UUID)

    @Query("delete from `basket` where id_purchase = :purchaseId and (select id_basket from history where id_basket=basket.id) is null")
    fun deleteAllByPurchase(purchaseId: UUID)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(check: BasketEntity): Long

    @Update
    fun update(check: BasketEntity): Int

    @Transaction
    fun insertOrUpdate(basket: BasketEntity) {
        val id = insert(basket)
        if (id ==-1L)
            update(basket)
    }
    @Insert
    fun add(basket: BasketEntity)

}