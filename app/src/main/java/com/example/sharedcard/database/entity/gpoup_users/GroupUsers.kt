package com.example.sharedcard.database.entity.gpoup_users

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID

data class GroupUsers(
    @Embedded
    val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_group",
        entity = GroupUsersEntity::class
    )
    val users: List<UserEntityWithStatus>
)

data class UserEntityWithStatus(
    private val id_user: UUID,
    val status: Int,
    @Relation(
        parentColumn = "id_user",
        entityColumn = "id"
    )
    val user: UserEntity
)