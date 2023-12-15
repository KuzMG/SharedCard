package com.example.sharedcard.database.entity.check

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "check")
data class CheckEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_check")
    val idCheck: Long? = null,
    @field:ColumnInfo("id_product_thing")
    val idProductThing: Long,
    val flag: Boolean,
    val description: String,
    val status: Int = 0,
    @field:ColumnInfo("id_group")
    val idGroup: Long = 0,
    @field:ColumnInfo("id_shop")
    val idShop: Long? = null,
    val price: Int = 0,
    val count: Int,
    @field:ColumnInfo("id_metric")
    val idMetric: Long,
    @field:ColumnInfo("id_currency")
    val idCurrency: Long? = null,
    @field:ColumnInfo("id_user_creator")
    val idCreator: Long? = null,
    @field:ColumnInfo("id_user_buyer")
    val idBuyer: Long? = null,
    @field:ColumnInfo(name = "date_first")
    val dataFirst: Long = Date().time,
    @field:ColumnInfo(name = "date_last")
    val dataLast: Long? = null
)

