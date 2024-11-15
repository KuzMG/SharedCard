package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UpdateGroupResponse(
    val name: String,
    @SerializedName("group_id")
    val groupId: UUID
)