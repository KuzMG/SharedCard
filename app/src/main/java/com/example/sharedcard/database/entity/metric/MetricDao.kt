package com.example.sharedcard.database.entity.metric

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MetricDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(metricsEntity: List<MetricEntity>)

    @Query("Select * from metric")
    fun getAll(): LiveData<List<MetricEntity>>
    @Query("Select * from metric where id =:id")
    fun getById(id: Int): MetricEntity
}