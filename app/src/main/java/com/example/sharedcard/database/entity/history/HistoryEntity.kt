package com.example.sharedcard.database.entity.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.group.GroupEntity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "history",
    primaryKeys = ["id_basket"],
)
data class HistoryEntity(
    @ColumnInfo("id_basket")
    @SerializedName("id_basket")
    val idBasket: UUID,
    @ColumnInfo("id_person")
    @SerializedName("id_person")
    val idPerson: UUID,
    @ColumnInfo("id_shop")
    @SerializedName("id_shop")
    val idShop: Int,
    val price: Double = 0.0,
    @ColumnInfo(name = "purchase_date")
    @SerializedName("purchase_date")
    val purchaseDate: Long = Date().time
)

