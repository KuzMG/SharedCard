package com.example.sharedcard.database.entity.user

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
