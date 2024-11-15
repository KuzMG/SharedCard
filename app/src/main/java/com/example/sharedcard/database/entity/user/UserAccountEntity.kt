package com.example.sharedcard.database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity(
    tableName = "user_account",
    primaryKeys = ["id"]
)
data class UserAccountEntity(
    val id: UUID,
    val email: String,
    val password: String
)
