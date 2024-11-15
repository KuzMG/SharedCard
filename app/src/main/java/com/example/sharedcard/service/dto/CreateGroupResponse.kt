package com.example.sharedcard.service.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CreateGroupResponse(
    val name: String,
    @SerializedName("user_id")
    val userId: UUID,
    val pic: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateGroupResponse

        if (name != other.name) return false
        if (userId != other.userId) return false
        if (!pic.contentEquals(other.pic)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + pic.contentHashCode()
        return result
    }
}