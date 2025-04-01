package com.example.sharedcard.database.entity.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.util.UUID

@Dao
interface HistoryDao {

    @Query("delete from purchase where id = :id")
    fun delete(id: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(purchases: List<HistoryEntity>)

    @Insert()
    fun add(purchases: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(purchase: HistoryEntity): Long

    @Update
    fun update(purchase: HistoryEntity): Int

    @Transaction
    fun insertOrUpdate(purchase: HistoryEntity) {
        val id = insert(purchase)
        if (id == -1L)
            update(purchase)
    }


    fun basketToHistory() {
        TODO("Not yet implemented")
    }

    @Query("select count(*) from history where id_basket = (select id from basket where id_purchase= :purchaseId)")
    fun getCount(purchaseId: UUID) : Int

    @Query("select history.id_person,id_purchase,purchase.purchase_date, sum(history.price) as price, sum(basket.count) as count, id_shop from history " +
            "    join basket on basket.id = history.id_basket " +
            "    join purchase on purchase.id = basket.id_purchase " +
            "        where history.id_person in (select id_person from group_persons where id_group in " +
            "        (select id_group from group_persons where group_persons.id_person =:personId)) " +
            "        group by id_purchase,purchase.purchase_date,history.id_shop, history.id_person " +
            "        order by purchase.purchase_date")

    fun getAll(personId: UUID): LiveData<List<History>>

}