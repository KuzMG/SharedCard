package com.example.sharedcard.database.entity.purchase

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sharedcard.database.entity.group.GroupEntity

data class PurchaseGroup(
    @Embedded
    val group: GroupEntity,
    @Relation(parentColumn = "id",
        entityColumn = "id_group",
        entity = PurchaseEntity::class)
    val purchases: List<Purchase>

)
