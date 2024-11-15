package com.example.sharedcard.database.entity.gpoup_users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sharedcard.database.entity.user.UserEntity
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID

@Dao
interface GroupUsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entities: List<GroupUsersEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: GroupUsersEntity): Long

    @Update
    fun update(entity: GroupUsersEntity): Int

    @Transaction
    fun insertOrUpdate(entity: GroupUsersEntity) {
        val id = insert(entity)
        if (id ==-1L)
            update(entity)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: GroupUsersEntity)
    @Insert
    fun createGroups(entities: List<GroupUsersEntity>)
    @Query("delete from group_users where id_group=:idGroup")
    fun deleteGroup(idGroup: UUID)
    @Query("select status from group_users where id_user=:idUser and id_group=:idGroup")
    fun getStatus(idUser: UUID, idGroup: UUID): LiveData<Int>

    @Query("update group_users set status = 1 where id_user=:idUser and id_group=:idGroup")
    fun setStatusOnAdmin(idUser: UUID, idGroup: UUID)
    @Query("select * from `group` " +
            "join group_users as gu on `group`.id = gu.id_group " +
            "where gu.id_group in (select id_group from group_users where id_user=:id) and `group`.name!='' group by `group`.id;")
    fun allGroup(id: UUID): LiveData<List<GroupUsers>>
    @Query("delete from group_users where id_user=:idUser and id_group=:idGroup")
    fun deleteUser(idGroup:UUID,idUser: UUID)


}