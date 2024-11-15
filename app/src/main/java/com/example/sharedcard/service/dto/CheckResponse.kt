package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.check.CheckEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CheckResponse(
    val check: CheckEntity,
    @SerializedName("group_id")
    val groupId: UUID
)