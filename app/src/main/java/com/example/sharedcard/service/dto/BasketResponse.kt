package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class BasketResponse(
    val basket: BasketEntity,
    @SerializedName("group_id")
    val groupId: UUID
)