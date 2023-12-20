package com.example.sharedcard.database.entity.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.target.TargetEntity

@Dao
interface TargetDao {
    @Query("select target.id,target.name,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and target.status = 0 " +
            "order by date_first desc")
    fun getAllForCheck(id: Long): LiveData<List<Target>>

    @Query("select target.id,target.name,c.name as category,first_price as price,cur.name as currency,u.name as creator,date_first as dateFirst from target " +
            "join category as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "left join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and target.status = 0 and target.name like :query " +
            "order by date_first desc")
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Target>>

    @Query("select target.id,target.name,c.name as category,first_price,curF.name as currencyFirst,last_price,curL.name as currencyLast,u.name as user,sh.name as shop,date_last as dateLast from target " +
            "join category as c on id_category = c.id " +
            "join shop as sh on id_shop = sh.id " +
            "join currency as curF on id_currency_first = curF.id " +
            "join currency as curL on id_currency_last = curL.id " +
            "join user as u on id_user_buyer = u.id_user " +
            "where target.id_group = :id and target.status = 1 " +
            "order by date_first desc")
    fun getAllForHistory(id: Long): LiveData<List<TargetHistory>>

    @Query("select target.id,target.name,c.name as category,first_price,curF.name as currencyFirst,last_price,curL.name as currencyLast,u.name as user,sh.name as shop,date_last as dateLast from target " +
            "join category as c on id_category = c.id " +
            "join shop as sh on id_shop = sh.id " +
            "join currency as curF on id_currency_first = curF.id " +
            "join currency as curL on id_currency_last = curL.id " +
            "join user as u on id_user_buyer = u.id_user " +
            "where target.id_group = :id and target.status = 1 and target.name like :query " +
            "order by date_first desc")
    fun getAllForHistoryQuery(id: Long, query: String): LiveData<List<TargetHistory>>

    @Insert
    fun add(target: TargetEntity)
    @Query("update target set status = :status where id=:id")
    fun uprateStatus(id: Long, status: Int)

    @Query("delete from target where id = :id")
    fun delete(id: Long)

    @Query(
        "update target set " +
                "status = 1, " +
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