package com.example.sharedcard.database.entity.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.target.TargetEntity

@Dao
interface TargetDao {
    @Query("select target.id,target.name,status,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category_target as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "left join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and status != 2 " +
            "order by status, date_first desc")
    fun getAllForCheck(id: Long): LiveData<List<Target>>

    @Query("select target.id,target.name,status,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category_target as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "left join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and status != 2 and target.name like :query " +
            "order by status, date_first desc")
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Target>>

    @Insert
    fun add(target: TargetEntity)
    @Query("update target set status = :status where id=:id")
    fun uprateStatus(id: Long, status: Int)

    @Query("delete from target where id = :id")
    fun delete(id: Long)

    @Query(
        "update target set " +
                "status = 2, " +
                "id_shop = :idShop, " +
                "last_price = :price, " +
                "id_currency_last = :currency, " +
                "id_user_buyer = :idUser, " +
                "date_last = :dataLast " +
                "where id = :id"
    )
    fun toHistory(
        id: Long,
        idShop: Long,
        price: Int,
        currency: Long,
        idUser: Long?,
        dataLast: Long
    )
}