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
    fun createGroup(group: GroupEntity)

    @Insert
    fun createGroups(groups: List<GroupEntity>)

    @Query("select * from `group` where id_group=:id")
    fun getGroup(id: UUID): LiveData<GroupEntity>

    @Update
    fun update(entity: GroupEntity)
}