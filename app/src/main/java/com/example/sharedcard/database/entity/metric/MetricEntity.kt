package com.example.sharedcard.database.entity.metric

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "metric"
)
data class MetricEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)
