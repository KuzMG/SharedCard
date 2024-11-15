package com.example.sharedcard.database.entity.currency

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: List<CurrencyEntity>)

    @Query("select * from currency")
    fun getAll(): LiveData<List<CurrencyEntity>>
}