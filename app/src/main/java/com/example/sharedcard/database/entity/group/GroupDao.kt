package com.example.sharedcard.database.entity.group

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
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

    @Query("select * from group_persons where id_person=:personId and (select name from `group` where id=group_persons.id_group)=''")
    fun findLocalGroup(personId: UUID): GroupPersonsEntity

    @Query("select * from `group` where id=:id")
    fun getGroupLiveData(id: UUID): LiveData<GroupEntity>

    @Query("select * from `group` where id=:id")
    fun getGroup(id: UUID): GroupEntity



    @Query("update `group` set name=:name where id=:id")
    fun setName(id: UUID, name: String)

    @Query("delete from `group` where name != ''")
    fun deleteAllGroups()
    @Query("select * from `group` where id in (select id_group from group_persons where id_person=:personId) order by name asc")
    fun getAll(personId: UUID): LiveData<List<GroupEntity>>
    @Query("select * from `group` where id in " +
            "(select id_group from group_persons where id_person=:personId and id_group not in (:excludeGroupsSet)) " +
            "and (select count(*) from purchase where purchase.id_group=`group`.id) >0 order by name asc")
    fun getAll(personId: UUID, excludeGroupsSet: Set<UUID>): LiveData<List<GroupEntity>>
    @Query("select * from `group` where id in (select id_group from group_persons where id_person=:personId) and name != '' order by name asc")
    fun getAllWithoutDefault(personId: UUID): LiveData<List<GroupEntity>>
}