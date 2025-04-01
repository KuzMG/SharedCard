package com.example.sharedcard.database.entity.basket

import androidx.room.Relation
import com.example.sharedcard.database.entity.history.HistoryEntity
import java.util.UUID

data class Basket(
    val id: UUID,
    val count: Double,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_basket"
    )
    val history: HistoryEntity?
)