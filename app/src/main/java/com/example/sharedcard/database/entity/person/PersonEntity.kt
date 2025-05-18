package com.example.sharedcard.database.entity.person

import androidx.room.Entity
import com.example.sharedcard.di.module.ServiceModule
import com.example.sharedcard.ui.purchase.adapters.GroupListAdapter
import java.util.UUID

@Entity(
    tableName = "person",
    primaryKeys = ["id"]
)
data class PersonEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val birthday: Long,
    val gender: Boolean,
    val pic: String
) : GroupListAdapter.GroupItem{
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
    override val _id: UUID
        get() = id
    override val _name: String
        get() = name
    override val _pic: String
        get() = url
}