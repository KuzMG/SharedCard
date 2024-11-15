package com.example.sharedcard.database.entity.metric

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "metric",
    primaryKeys = ["id"]
)
data class MetricEntity(
    val id: Int,
    val name: String,
    val name_en: String
)
