package com.project.shared_card.database.dao.shop_target

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_target")
class ShopTargetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)