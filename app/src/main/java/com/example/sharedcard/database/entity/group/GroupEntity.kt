package com.example.sharedcard.database.entity.group

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "group",
    primaryKeys = ["id_group"]
)
data class GroupEntity(
    @ColumnInfo("id_group")
    val id: UUID = UUID.randomUUID(),
    val name: String
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
