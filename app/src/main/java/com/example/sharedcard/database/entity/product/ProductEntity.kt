package com.project.shared_card.database.dao.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "product")
data class ProductEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val status: Int,
    @field:ColumnInfo("id_group")
    val idGroup: Long,
    @field:ColumnInfo("id_category")
    val idCategory: Long,
    @field:ColumnInfo("id_shop")
    val idShop: Long,
    val price: Int,
    val count: Int,
    @field:ColumnInfo("id_metric")
    val idMetric: Long,
    @field:ColumnInfo("id_currency")
    val idCurrency: Long,
    @field:ColumnInfo("id_user_creator")
    val idCreator: Long,
    @field:ColumnInfo("id_user_buyer")
    val idBuyer: Long,
    @field:ColumnInfo(name = "date_first")
    val dataFirst: Long,
    @field:ColumnInfo(name = "date_last")
    val dataLast: Long
)

