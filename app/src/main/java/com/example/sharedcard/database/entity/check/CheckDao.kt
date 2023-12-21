package com.example.sharedcard.database.entity.check

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.UUID

@Dao
interface CheckDao {
    @Query("select `check`.id_check as id_check, pr.name as name,c.name as category,u.name as user,m.name as metric,count,date_first,`check`.status as status,description,u.id_user as idUser,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                        "join product as pr on `check`.id_product = pr.id " +
                        "join category as c on pr.id_category = c.id " +
                        "join user as u on u.id_user = id_user_creator " +
                        "join metric as m on m.id = id_metric " +
                        "where id_group = :id and `check`.status != 2 " +
                        "order by `check`.status, date_first desc"
    )
    fun getAllForCheck(id: UUID): LiveData<List<Check>>

    @Query("select `check`.id_check as id_check, pr.name as name,c.name as category,u.name as user,m.name as metric,count,date_first,`check`.status as status,description,u.id_user as idUser,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
            "join product as pr on `check`.id_product = pr.id " +
            "join category as c on pr.id_category = c.id " +
            "join user as u on u.id_user = id_user_creator " +
            "join metric as m on m.id = id_metric " +
            "where id_group = :id and `check`.status != 2 and pr.name like :query " +
            "order by `check`.status, date_first desc"
    )
    fun getAllForCheckQuery(id: UUID, query: String): LiveData<List<Check>>

    @Query("select `check`.id_check as id_check,pr.name as name,c.name as category,u.name as user,sh.name as shop,m.name as metric,count,price,cur.name as currency,date_last,u.id_user as idUser,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
            "join product as pr on `check`.id_product = pr.id " +
            "join shop as sh on sh.id = id_shop " +
            "join category as c on pr.id_category = c.id " +
            "join user as u on u.id_user = id_user_buyer " +
            "join metric as m on m.id = id_metric " +
            "join currency as cur on cur.code = id_currency " +
            "where id_group = :id and `check`.status = 2 " +
            "order by date_last desc"
    )
    fun getAllForHistory(id: UUID): LiveData<List<CheckHistory>>

    @Query("select `check`.id_check as id_check,pr.name as name,c.name as category,u.name as user,sh.name as shop,m.name as metric,count,price,cur.name as currency,date_last,u.id_user as idUser,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
            "join product as pr on `check`.id_product = pr.id " +
            "join shop as sh on sh.id = id_shop " +
            "join category as c on pr.id_category = c.id " +
            "join user as u on u.id_user = id_user_buyer " +
            "join metric as m on m.id = id_metric " +
            "join currency as cur on cur.code = id_currency " +
            "where id_group = :id and `check`.status = 2 and pr.name like :query " +
            "order by date_last desc"
    )
    fun getAllForHistoryQuery(id: UUID, query: String): LiveData<List<CheckHistory>>
    @Query("delete from `check` where id_check = :id")
    fun delete(id: UUID)

    @Insert
    fun add(check: CheckEntity)

    @Query("update `check` set status = :status where id_check=:id")
    fun uprateStatus(id: UUID, status: Int)

    @Query(
        "update `check` set " +
                "status = 2, " +
                "id_shop = :idShop, " +
                "price = :price, " +
                "id_currency = :currency, " +
                "id_user_buyer = :idUser, " +
                "date_last = :dataLast " +
                "where id_check=:id"
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