package com.project.shared_card.database.dao.category_product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_product")
class CategoryProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)