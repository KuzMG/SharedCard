package com.example.sharedcard.database.entity.group

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(group: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(group: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(group: GroupEntity): Long

    @Update
    fun update(group: GroupEntity): Int

    @Transaction
    fun insertOrUpdate(group: GroupEntity) {
        val id = insert(group)
        if (id ==-1L)
            update(group)
    }

    @Query("delete from `group` where id=:idGroup")
    fun delete(idGroup: UUID)

    @Insert
    fun createGroups(groups: List<GroupEntity>)

    @Query("select * from group_users where id_user=:idUser and (select name from `group` where id=group_users.id_group)=''")
    fun findLocalGroup(idUser: UUID): GroupUsersEntity

    @Query("select * from `group` where id=:id")
    fun getGroupLiveData(id: UUID): LiveData<GroupEntity>

    @Query("select * from `group` where id=:id")
    fun getGroup(id: UUID): GroupEntity

    @Query("update `group` set name=:name where id=:id")
    fun setName(id: UUID, name: String)

    @Query("delete from `group` where name != ''")
    fun deleteAllGroups()
}