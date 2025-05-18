package com.example.sharedcard.database.entity.basket

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.entity.shop.ShopEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Basket(
    val id: UUID,
    val count: Double,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_basket",
        entity = HistoryEntity::class
    )
    val history: History?
){
    data class History(
        @ColumnInfo("id_basket")
        val idBasket: UUID,
        @ColumnInfo("id_person")
        val idPerson: UUID,
        @Relation(
            parentColumn = "id_person",
            entityColumn = "id"
        )
        val person:PersonEntity,
        val price: Double,
        @ColumnInfo("id_shop")
        val idShop: Int,
        @Relation(
            parentColumn = "id_shop",
            entityColumn = "id"
        )
        val shop:ShopEntity,
        @ColumnInfo("purchase_date")
        val purchaseDate: Long
    )
}