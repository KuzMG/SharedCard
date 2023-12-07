package com.example.sharedcard.database.entity.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun createUser(user: UserEntity)

    @Insert
    fun createUsers(users: List<UserEntity>)
    @Query("select * from user where id_user = :id")
    fun get(id: Long): LiveData<UserEntity>
    @Update
    fun update(entity: UserEntity)

    @Delete
    fun delete(entity: UserEntity)
}