package com.example.sharedcard.database.entity.target

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.UUID

@Dao
interface TargetDao {
    @Query("select target.id,target.name,c.name as category,first_price as price,cur.symbol as currency,u.name as user,u.id_user,date_first as dateFirst from target " +
            "join category as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and target.status = 0 " +
            "order by date_first desc")
    fun getAllForCheck(id: UUID): LiveData<List<Target>>

    @Query("select target.id,target.name,c.name as category,first_price as price,cur.symbol as currency,u.name as user,u.id_user,date_first as dateFirst from target " +
            "join category as c on id_category = c.id " +
            "join currency as cur on id_currency_first = cur.id " +
            "join user as u on id_user_creator = u.id_user " +
            "where target.id_group = :id and target.status = 0 and target.name like :query " +
            "order by date_first desc")
    fun getAllForCheckQuery(id: UUID, query: String): LiveData<List<Target>>

    @Query("select target.id,target.name,c.name as category,first_price,curF.symbol as currencyFirst,last_price,curL.symbol as currencyLast,uf.name as userFirst,uf.id_user as idUserFirst,ul.name as userLast,ul.id_user as idUserLast,sh.name as shop,date_last as dateLast from target " +
            "join category as c on id_category = c.id " +
            "join shop as sh on id_shop = sh.id " +
            "join currency as curF on id_currency_first = curF.id " +
            "join currency as curL on id_currency_last = curL.id " +
            "join user as uf on id_user_creator = uf.id_user " +
            "join user as ul on id_user_buyer = ul.id_user " +
            "where target.id_group = :id and target.status = 1 " +
            "order by date_first desc")
    fun getAllForHistory(id: UUID): LiveData<List<TargetHistory>>

    @Query("select target.id,target.name,c.name as category,first_price,curF.symbol as currencyFirst,last_price,curL.symbol as currencyLast,uf.name as userFirst,uf.id_user as idUserFirst,ul.name as userLast,ul.id_user as idUserLast,sh.name as shop,date_last as dateLast from target " +
            "join category as c on id_category = c.id " +
            "join shop as sh on id_shop = sh.id " +
            "join currency as curF on id_currency_first = curF.id " +
            "join currency as curL on id_currency_last = curL.id " +
            "join user as uf on id_user_creator = uf.id_user " +
            "join user as ul on id_user_buyer = ul.id_user " +
            "where target.id_group = :id and target.status = 1 and target.name like :query " +
            "order by date_first desc")
    fun getAllForHistoryQuery(id: UUID, query: String): LiveData<List<TargetHistory>>

    @Insert
    fun add(target: TargetEntity)
    @Query("update target set status = :status where id=:id")
    fun uprateStatus(id: UUID, status: Int)

    @Query("delete from target where id = :id")
    fun delete(id: UUID)

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
        id: UUID,
        idShop: Long,
        price: Int,
        currency: Long,
        idUser: UUID,
        dataLast: Long
    )
}