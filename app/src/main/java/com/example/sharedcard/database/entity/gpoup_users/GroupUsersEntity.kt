package com.project.shared_card.database.dao.group_users

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(
    tableName = "group_users",
    primaryKeys = ["id_user", "id_group"],

)
data class GroupUsersEntity(
    @ColumnInfo(name = "id_user")
    @SerializedName("id_user")
    val idUser: UUID,
    @ColumnInfo(name = "id_group")
    @SerializedName("id_group")
    val idGroup: UUID,
    val status: Int
){
    companion object{
        const val CREATOR = 2
        const val ADMIN = 1
        const val USER = 0
    }
}