package com.example.sharedcard.database.entity.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.shared_card.database.dao.target.TargetEntity

@Dao
interface TargetDao {
    @Query("select target.id,target.name,status,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category_target as c on id_category = c.id " +
            "join currency as cur on id_currency = cur.id " +
            "left join user as u on id_user_creator = u.id " +
            "where target.id_group = :id and status != 2 " +
            "order by status, date_first desc")
    fun getAllForCheck(id: Long): LiveData<List<Target>>

    @Query("select target.id,target.name,status,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category_target as c on id_category = c.id " +
            "join currency as cur on id_currency = cur.id " +
            "left join user as u on id_user_creator = u.id " +
            "where target.id_group = :id and status != 2 and target.name like :query " +
            "order by status, date_first desc")
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Target>>

    @Query("select * from target where id_group = :id and status = 2 order by status")
    fun getAllForHistory(id: Long): LiveData<List<TargetEntity>>

    @Insert
    fun add(target: TargetEntity)
    @Query("update target set status = :status where id=:id")
    fun uprateStatus(id: Long, status: Int)


    @Update
    fun update(entity: TargetEntity)

    @Delete
    fun delete(entity: TargetEntity)
}