package com.example.sharedcard.database.entity.purchase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.sharedcard.database.entity.group.GroupEntity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "purchase",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(GroupEntity::class, ["id"], ["id_group"], ForeignKey.CASCADE)]
)
data class PurchaseEntity(
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("id_product")
    @SerializedName("id_product")
    val idProduct: Int,
    @ColumnInfo("id_currency")
    @SerializedName("id_currency")
    val idCurrency: Int,
    @ColumnInfo("id_group")
    @SerializedName("id_group")
    val idGroup: UUID,
    @ColumnInfo("id_person")
    @SerializedName("id_person")
    val idPerson: UUID,
    @ColumnInfo("is_bought")
    @SerializedName("is_bought")
    val isBought: Boolean = false,
    val count: Double,
    val price: Double,
    val description: String,
    @ColumnInfo(name = "creation_date")
    @SerializedName("creation_date")
    val creationDate: Long = Date().time,
    @ColumnInfo(name = "purchase_date")
    @SerializedName("purchase_date")
    val purchaseDate: Long? = null
){
    companion object{
        fun PurchaseEntity.toHistory() =
            PurchaseEntity(
                id,
                idProduct,
                idCurrency,
                idGroup,
                idPerson,
                true,
                count,
                price,
                description,
                creationDate,
                Date().time
            )

    }
}

