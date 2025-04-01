package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class PurchaseResponse(
    val purchase: PurchaseEntity,
    @SerializedName("group_id")
    val groupId: UUID
)