package com.example.sharedcard.database.entity.group

import androidx.room.Entity
import androidx.room.Ignore

@Entity(
    tableName = "group",
    primaryKeys = ["id"]
)
data class GroupEntity(
    val id: Long,
    val name: String
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
