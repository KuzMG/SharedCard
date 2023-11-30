package com.project.shared_card.database.dao.target

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target")
data class TargetEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val status: Int,
    @field:ColumnInfo(name = "id_group")
    val idGroup: Long,
    @field:ColumnInfo(name = "id_category")
    val idCategory: Long,
    @field:ColumnInfo(name = "id_shop")
    val idShop: Long,
    val price: Int,
    @field:ColumnInfo(name = "id_currency")
    val idCurrency: Long,
    @field:ColumnInfo(name = "id_user_creator")
    val creator: Long,
    @field:ColumnInfo(name = "id_user_buyer")
    val buyer: Long,
    @field:ColumnInfo(name = "date_first")
    val dateFirst: Long,
    @field:ColumnInfo(name = "date_last")
    val dateLast: Long
)




