package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import com.project.shared_card.database.dao.target.TargetEntity

data class AccountResponse(
    val users: List<UserEntity>,
    val groups: List<GroupEntity>,
    val groupUsers: List<GroupUsersEntity>,
    val checks: List<CheckEntity>,
    val targets: List<TargetEntity>
)