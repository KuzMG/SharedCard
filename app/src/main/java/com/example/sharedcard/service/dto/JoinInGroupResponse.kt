package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class JoinInGroupResponse(
    val token: String,
    @SerializedName("user_id")
    val userId: UUID
)