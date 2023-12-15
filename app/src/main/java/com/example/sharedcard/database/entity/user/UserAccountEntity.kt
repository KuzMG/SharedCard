package com.example.sharedcard.database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
class UserAccountEntity(
    @PrimaryKey
    val id: Long,
    val pswdhash: String,
    val login: String,
    @ColumnInfo("create_date")
    val createDate: Long,
    @ColumnInfo("update_date")
    val updateDate:Long
)