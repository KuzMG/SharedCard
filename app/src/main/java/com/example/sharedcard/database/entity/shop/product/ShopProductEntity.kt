package com.project.shared_card.database.dao.shop_product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_product")
class ShopProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)