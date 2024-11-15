package com.example.sharedcard.database.entity.check

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sharedcard.database.entity.group.GroupEntity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "check",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(GroupEntity::class, ["id"], ["id_group"], ForeignKey.CASCADE)]
)
data class CheckEntity(
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("id_product")
    @SerializedName("id_product")
    val idProduct: Int,
    val description: String,
    val status: Int = 0,
    @ColumnInfo("id_group")
    @SerializedName("id_group")
    val idGroup: UUID,
    @ColumnInfo("id_shop")
    @SerializedName("id_shop")
    val idShop: Int? = null,
    val price: Int = 0,
    val count: Int,
    @ColumnInfo("id_metric")
    @SerializedName("id_metric")
    val idMetric: Int,
    @ColumnInfo("id_currency")
    @SerializedName("id_currency")
    val idCurrency: Int? = null,
    @ColumnInfo("id_creator")
    @SerializedName("id_creator")
    val idCreator: UUID,
    @ColumnInfo("id_buyer")
    @SerializedName("id_buyer")
    val idBuyer: UUID? = null,
    @ColumnInfo(name = "date_first")
    @SerializedName("date_first")
    val dataFirst: Long = Date().time,
    @ColumnInfo(name = "date_last")
    @SerializedName("date_last")
    val dataLast: Long? = null
){
    companion object{
        const val HISTORY = 2
        const val CHECKED = 1
        const val UNCHECKED = 0
    }
}

