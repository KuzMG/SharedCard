package com.example.sharedcard.service.dto


import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.UUID

data class AccountDeleteResponse(
    val users: List<UUID>,
    val groups: List<UUID>,
    val groupUsers: List<Pair<UUID,UUID>>,
    val checks: List<UUID>,
    val targets: List<UUID>
)