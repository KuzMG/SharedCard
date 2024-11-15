package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class SetUserStatusResponse(
    @SerializedName("user_admin_id")
    val userAdminId: UUID,
    @SerializedName("group_id")
    val groupId: UUID,
    @SerializedName("user_id")
    val userId: UUID,
    val status: Int
)
