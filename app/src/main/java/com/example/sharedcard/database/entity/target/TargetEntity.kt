package com.project.shared_card.database.dao.target

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "target")
data class TargetEntity(
    @field:PrimaryKey()
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val status: Int = 0,
    @field:ColumnInfo(name = "id_group")
    val idGroup: UUID,
    @field:ColumnInfo(name = "id_category")
    val idCategory: Long,
    @field:ColumnInfo(name = "id_shop")
    val idShop: Long? = null,
    @field:ColumnInfo(name = "first_price")
    val firstPrice: Int,
    @field:ColumnInfo(name = "last_price")
    val lastPrice: Int? = null,
    @field:ColumnInfo(name = "id_currency_first")
    val idCurrencyFirst: Long,
    @field:ColumnInfo(name = "id_currency_last")
    val idCurrencyLast: Long? = null,
    @field:ColumnInfo(name = "id_user_creator")
    val idCreator: UUID,
    @field:ColumnInfo(name = "id_user_buyer")
    val idBuyer: UUID? = null,
    @field:ColumnInfo(name = "date_first")
    val dateFirst: Long = Date().time,
    @field:ColumnInfo(name = "date_last")
    val dateLast: Long? = null
)




