package com.project.shared_card.database.dao.group_users

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "group_users",
    primaryKeys = ["id_user", "id_group"]
)
data class GroupUsersEntity(
    @field:ColumnInfo(name = "id_user")
    val idUser: Long,
    @field:ColumnInfo(name = "id_group")
    val idGroup: Long,
    val status: Boolean
)