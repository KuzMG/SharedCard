package com.example.sharedcard.database.entity.gpoup_users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID

@Dao
interface GroupUsersDao {
    @Insert
    fun createGroup(entity: GroupUsersEntity)
    @Insert
    fun createGroups(entities: List<GroupUsersEntity>)
    @Query("delete from group_users where id_group=:idGroup")
    fun deleteGroup(idGroup: UUID)
    @Query("select status from group_users where id_user=:idUser and id_group=:idGroup")
    fun getStatus(idUser: UUID, idGroup: UUID): LiveData<Boolean>

    @Query("update group_users set status = 1 where id_user=:idUser and id_group=:idGroup")
    fun setStatusOnAdmin(idUser: UUID, idGroup: UUID)
    @Transaction
    @Query("select * from `group` " +
            "join group_users as gu on `group`.id_group = gu.id_group " +
            "where gu.id_group in (select id_group from group_users where id_user=:id) and `group`.name!='' group by `group`.id_group;")
    fun allGroup(id: UUID): LiveData<List<GroupUsers>>
    @Query("delete from group_users where id_user=:idUser and id_group=:idGroup")
    fun deleteUser(idGroup:UUID,idUser: UUID)
}