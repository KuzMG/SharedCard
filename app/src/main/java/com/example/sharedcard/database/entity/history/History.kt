package com.example.sharedcard.database.entity.history

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.entity.shop.ShopEntity
import java.util.Date
import java.util.UUID

data class History(
    @ColumnInfo("id_person")
    val idPerson: UUID,
    @ColumnInfo("id_purchase")
    val idPurchase: UUID,
    @ColumnInfo("purchase_date")
    private val purchaseDate: Long,
    val price: Double,
    val count: Double,
    @ColumnInfo("id_shop")
    val idShop: Int
) {
    val date: Date
        get() = Date(purchaseDate)

}
