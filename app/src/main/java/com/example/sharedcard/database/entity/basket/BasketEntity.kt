package com.example.sharedcard.database.entity.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "basket",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(PurchaseEntity::class, ["id"], ["id_purchase"], ForeignKey.CASCADE)]
)
data class BasketEntity(
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("id_purchase")
    @SerializedName("id_purchase")
    val idPurchase: UUID,
    val count: Double
)

