package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.check.CheckEntity
import com.google.gson.annotations.SerializedName
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.UUID

data class TargetResponse(
    private val target: TargetEntity,
    @SerializedName("group_id")
    private val groupId: UUID
)