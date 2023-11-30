package com.project.shared_card.database.dao.categories_target

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_target")
data class CategoryTargetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)