package com.example.sharedcard.database.entity.metric

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "metric",
    primaryKeys = arrayOf("id")
)
data class MetricEntity(
    val id: Long,
    val name: String,
    val name_en: String
)
