package com.example.sharedcard.database.entity.group

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface GroupDao {
    @Insert
    fun createGroup(group: GroupEntity): Long?
    @Query("delete from `group` where id_group=:idGroup")
    fun deleteGroup(idGroup: UUID)
    @Insert
    fun createGroups(groups: List<GroupEntity>)
    @Query("select id_group from group_users where id_user=:idUser and (select name from `group` where id_group=group_users.id_group)=''")
    fun findLocalGroup(idUser: UUID): UUID
    @Query("select * from `group` where id_group=:id")
    fun getGroup(id: UUID): LiveData<GroupEntity>
    @Query("update `group` set name=:name where id_group=:id")
    fun setName(id: UUID,name: String)
    @Update
    fun update(entity: GroupEntity)
}