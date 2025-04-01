package com.example.sharedcard.database.entity.metric

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "metric",
    primaryKeys = ["id"]
)
data class MetricEntity(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String
)
