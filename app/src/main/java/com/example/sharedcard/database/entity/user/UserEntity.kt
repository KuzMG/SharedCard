package com.example.sharedcard.database.entity.user

import androidx.room.Entity
import com.example.sharedcard.di.module.ServiceModule
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(
    tableName = "user",
    primaryKeys = ["id"]
)
data class UserEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val weight: Double,
    val height: Int,
    @SerializedName("date")
    val birthday: Long,
    val gender: Boolean,
    val pic: String
) {
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}