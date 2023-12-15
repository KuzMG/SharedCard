package com.example.sharedcard.database.entity.check_product

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    "check_product",
    primaryKeys = arrayOf("id_check", "id_product")
)
data class CheckProduct(
    @ColumnInfo("id_check")
    val idCheck: Long,
    @ColumnInfo("id_product")
    val idProduct: Long
)
