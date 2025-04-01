package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class DeleteResponse(
    @SerializedName("delete_id")
    private val deleteId: UUID,
    @SerializedName("group_id")
    private val groupId: UUID,
    @SerializedName("person_id")
private val userId: UUID
)