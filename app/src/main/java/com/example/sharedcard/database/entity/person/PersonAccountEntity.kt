package com.example.sharedcard.database.entity.person

import androidx.room.Entity
import java.util.UUID
@Entity(
    tableName = "account",
    primaryKeys = ["id"]
)
data class PersonAccountEntity(
    val id: UUID,
    val email: String,
    val password: String
)