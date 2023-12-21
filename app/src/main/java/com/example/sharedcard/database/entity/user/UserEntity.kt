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
    var id: UUID = UUID.randomUUID(),
    var name: String,
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
