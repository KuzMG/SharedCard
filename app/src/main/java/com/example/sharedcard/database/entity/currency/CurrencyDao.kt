package com.example.sharedcard.database.entity.currency

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.UUID

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: List<CurrencyEntity>)

    @Query("select * from currency")
    fun getAll(): LiveData<List<CurrencyEntity>>
    @Query("select * from currency where id =:id")
    fun get(id: Int): LiveData<CurrencyEntity>
    @Query("select * from currency where id = (select id_currency from purchase where purchase.id = (select id_purchase from basket where basket.id = :basketId))")
    fun getByBasketId(basketId: UUID): LiveData<CurrencyEntity>
}