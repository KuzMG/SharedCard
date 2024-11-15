package com.project.shared_card.database.dao.target

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sharedcard.database.entity.group.GroupEntity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "target",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(GroupEntity::class, ["id"], ["id_group"], ForeignKey.CASCADE)]
)
data class TargetEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val status: Int = 0,
    @ColumnInfo(name = "id_group")
    @SerializedName("id_group")
    val idGroup: UUID,
    @ColumnInfo(name = "id_category")
    @SerializedName("id_category")
    val idCategory: Int,
    @ColumnInfo(name = "id_shop")
    @SerializedName("id_shop")
    val idShop: Int? = null,
    @ColumnInfo(name = "price_first")
    @SerializedName("price_first")
    val priceFirst: Int,
    @ColumnInfo(name = "price_last")
    @SerializedName("price_last")
    val priceLast: Int? = null,
    @ColumnInfo(name = "id_currency_first")
    @SerializedName("id_currency_first")
    val idCurrencyFirst: Int,
    @ColumnInfo(name = "id_currency_last")
    @SerializedName("id_currency_last")
    val idCurrencyLast: Int? = null,
    @ColumnInfo(name = "id_creator")
    @SerializedName("id_creator")
    val idCreator: UUID,
    @ColumnInfo(name = "id_buyer")
    @SerializedName("id_buyer")
    val idBuyer: UUID? = null,
    @ColumnInfo(name = "date_first")
    @SerializedName("date_first")
    val dateFirst: Long = Date().time,
    @ColumnInfo(name = "date_last")
    @SerializedName("date_last")
    val dateLast: Long? = null
){
    companion object{
        const val HISTORY = 2
        const val CHECKED = 1
        const val UNCHECKED = 0
    }
}




