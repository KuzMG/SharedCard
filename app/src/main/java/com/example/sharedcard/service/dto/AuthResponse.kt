package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class AuthResponse(
    @SerializedName("person_id")
    val personId: UUID,
    @SerializedName("group_id")
    val groupId: UUID)