package com.example.sharedcard.database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "user"
)
data class UserEntity(
    @PrimaryKey()
    @ColumnInfo("id_user")
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val name: String,
    val weight: Int =170,
    val height: Int=60,
    val age: Int =25,
    val password: String
)