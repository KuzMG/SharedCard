package com.project.shared_card.database.dao.group_users

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "group_users",
    primaryKeys = ["id_user", "id_group"]
)
data class GroupUsersEntity(
    @field:ColumnInfo(name = "id_user")
    val idUser: UUID,
    @field:ColumnInfo(name = "id_group")
    val idGroup: UUID,
    val status: Boolean
)