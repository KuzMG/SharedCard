package com.example.sharedcard.database.entity.gpoup_person

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import java.util.UUID

data class GroupPersons(
    @Embedded
    val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_group",
        entity = GroupPersonsEntity::class
    )
    val persons: List<UserEntityWithStatus>
)

data class UserEntityWithStatus(
    @ColumnInfo("id_person")
    private val idPerson: UUID,
    val status: Int,
    @Relation(
        parentColumn = "id_person",
        entityColumn = "id"
    )
    val person: PersonEntity
)