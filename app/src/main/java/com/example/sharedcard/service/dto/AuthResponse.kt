package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class AuthResponse(
    @SerializedName("id_user")
    val idUser: UUID,
    @SerializedName("id_group")
    val idGroup: UUID)