package com.example.sharedcard.database.entity.purchase

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.currency.CurrencyEntity
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.di.module.ServiceModule
import com.example.sharedcard.util.toStringFormat
import java.util.Date
import java.util.UUID

data class Purchase(
    val id: UUID,
    @ColumnInfo("id_product")
    private val idProduct: Int,
    @Relation(
        parentColumn = "id_product",
        entityColumn = "id",
        entity = ProductEntity::class
    )
    val product: Product,
    @ColumnInfo("id_currency")
    private val idCurrency: Int,
    @Relation(
        parentColumn = "id_currency",
        entityColumn = "id"
    )
    val currency: CurrencyEntity,
    val count: Double,
    val price: Double,
    @ColumnInfo("id_person")
    private val idPerson: UUID,
    @Relation(
        parentColumn = "id_person",
        entityColumn = "id"
    )
    val person: PersonEntity,
    @ColumnInfo("id_group")
    val idGroup: UUID,
    val description: String,
    @ColumnInfo("creation_date")
    private val creationDate: Long
) {
    val cost: String
        get() = "${price.toStringFormat()} ${currency.symbol}"
    val personPic: String
        get() = ServiceModule.URL_REST + "/${person.url}"
    val quantity: String
        get() = "${count.toStringFormat()} ${product.metric.name}"
    val date: Date
        get() = Date(creationDate)


}
