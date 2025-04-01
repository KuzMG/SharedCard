package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class HistoryResponse(
    val history: HistoryEntity,
    @SerializedName("group_id")
    val groupId: UUID
)