package com.example.sharedcard.database.entity.group

import androidx.room.Entity
import com.example.sharedcard.di.module.ServiceModule
import java.util.UUID

@Entity(
    tableName = "group",
    primaryKeys = ["id"]
)
data class GroupEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val pic: String
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}
