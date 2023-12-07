package com.example.sharedcard.database.entity.group

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "group",
    primaryKeys = ["id_group"]
)
data class GroupEntity(
    @ColumnInfo("id_group")
    val id: Long,
    val name: String
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
