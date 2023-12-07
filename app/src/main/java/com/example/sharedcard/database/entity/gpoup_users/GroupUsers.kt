package com.example.sharedcard.database.entity.gpoup_users

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity

data class GroupUsers(
    @Embedded
    val group: GroupEntity,
    @Relation(
        parentColumn = "id_group",
        entityColumn = "id_group",
        entity = GroupUsersEntity::class
    )
    val users: List<UserEntityWithStatus>
)

data class UserEntityWithStatus(
    private val id_user: Long,
    val status: Boolean,
    @Relation(
        parentColumn = "id_user",
        entityColumn = "id_user"
    )
    val user: UserEntity
)