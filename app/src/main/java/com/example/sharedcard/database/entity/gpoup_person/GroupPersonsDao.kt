package com.example.sharedcard.database.entity.gpoup_person

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
interface GroupPersonsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entities: List<GroupPersonsEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: GroupPersonsEntity): Long

    @Update
    fun update(entity: GroupPersonsEntity): Int

    @Transaction
    fun insertOrUpdate(entity: GroupPersonsEntity) {
        val id = insert(entity)
        if (id ==-1L)
            update(entity)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: GroupPersonsEntity)
    @Insert
    fun createGroups(entities: List<GroupPersonsEntity>)
    @Query("delete from group_persons where id_group=:idGroup")
    fun deleteGroup(idGroup: UUID)
    @Query("select status from group_persons where id_person=:personId and id_group=:groupId")
    fun getStatus(personId: UUID, groupId: UUID): LiveData<Int>

    @Query("update group_persons set status = 1 where id_person=:personId and id_group=:groupId")
    fun setStatusOnAdmin(personId: UUID, groupId: UUID)
    @Query("select * from `group` " +
            "join group_persons as gu on `group`.id = gu.id_group " +
            "where gu.id_group in (select id_group from group_persons where id_person=:id) and `group`.name!='' group by `group`.id;")
    fun allGroup(id: UUID): LiveData<List<GroupPersons>>
    @Query("delete from group_persons where id_person=:personId and id_group=:groupId")
    fun deletePerson(groupId:UUID, personId: UUID)


}