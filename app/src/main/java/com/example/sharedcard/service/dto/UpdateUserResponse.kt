package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.user.UserEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UpdateUserResponse(
    val user: UserEntity
)