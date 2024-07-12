package com.example.sharedcard.database.entity.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface UserDao {
    @Insert
    fun createUser(user: UserEntity)

    @Insert
    fun createUserAccount(user: UserAccountEntity)
    @Query("select * from user_account where email =:email")
    fun findUserAccount(email: String): UserAccountEntity
    @Insert
    fun createUsers(users: List<UserEntity>)
    @Query("select * from user where id_user = :id")
    fun get(id: UUID): LiveData<UserEntity>

    @Query("select * from user_account where id_user = :id")
    fun getAccount(id: UUID): LiveData<UserAccountEntity>
    @Update
    fun update(entity: UserEntity)
    @Query("update user set name = :name where  id_user = :id")
    fun setName(id: UUID,name: String)
    @Query("update user set weight = :weight where  id_user = :id")
    fun setWeight(id: UUID,weight: Int)
    @Query("update user set height = :height where  id_user = :id")
    fun setHeight(id: UUID,height: Int)
    @Query("update user set age = :age where  id_user = :id")
    fun setAge(id: UUID,age: Int)

    @Delete
    fun delete(entity: UserEntity)
}