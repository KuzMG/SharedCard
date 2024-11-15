package com.example.sharedcard.database.entity.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sharedcard.database.entity.group.GroupEntity
import java.util.UUID

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(users: List<UserEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity): Long

    @Update
    fun update(user: UserEntity): Int

    @Transaction
    fun insertOrUpdate(user: UserEntity) {
        val id = insert(user)
        if (id ==-1L)
            update(user)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUserAccount(user: UserAccountEntity)
    @Query("select * from user_account where email =:email")
    fun findUserAccount(email: String): UserAccountEntity
    @Insert
    fun createUsers(users: List<UserEntity>)
    @Query("select * from user where id = :id")
    fun getLiveData(id: UUID): LiveData<UserEntity>
    @Query("select * from user where id = :id")
    fun get(id: UUID): UserEntity
    @Query("select * from user_account where id = :id")
    fun getAccountLiveData(id: UUID): LiveData<UserAccountEntity>

    @Query("select * from user_account where id = :id")
    fun getAccount(id: UUID): UserAccountEntity
    @Query("update user set name = :name where  id = :id")
    fun setName(id: UUID,name: String)
    @Query("update user set weight = :weight where  id = :id")
    fun setWeight(id: UUID,weight: Double)
    @Query("update user set height = :height where  id = :id")
    fun setHeight(id: UUID,height: Int)

    @Query("delete from `user` where id=:idUser")
    fun delete(idUser: UUID)
    @Query("select * from user_account")
    fun getAll(): List<UserAccountEntity>
    @Query("delete from `user` where id !=:idUser")
    fun deleteAllUsers(idUser: UUID)
}