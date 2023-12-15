package com.example.sharedcard.database.entity.check

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckDao {
    @Query("select `check`.id_check as id_check, pr.name as name,c.name as category,u.name as user,m.name as metric,count,date_first,status,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                        "join check_product as cpr on `check`.id_check = cpr.id_check " +
                        "join product as pr on cpr.id_check = pr.id " +
                        "join category as c on pr.id_category = c.id " +
                        "join user as u on u.id_user = id_user_creator " +
                        "join metric as m on m.id = id_metric " +
                        "where id_group = :id and status != 2 " +
                        "order by status, date_first desc"
    )
    fun getAllForCheck(id: Long): LiveData<List<Check>>

    @Query("select `check`.id_check as id_check, pr.name as name,c.name as category,u.name as user,m.name as metric,count,date_first,status,pr.calorie as calorie,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
            "join check_product as cpr on `check`.id_check = cpr.id_check " +
            "join product as pr on cpr.id_check = pr.id " +
            "join category as c on pr.id_category = c.id " +
            "join user as u on u.id_user = id_user_creator " +
            "join metric as m on m.id = id_metric " +
            "where id_group = :id and status != 2 and pr.name like :query " +
            "order by status, date_first desc"
    )
    fun getAllForCheckQuery(id: Long, query: String): LiveData<List<Check>>


    @Query("delete from `check` where id_check = :id")
    fun delete(id: Long)

    @Insert
    fun add(check: CheckEntity)

    @Query("update `check` set status = :status where id_check=:id")
    fun uprateStatus(id: Long, status: Int)

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
        id: Long,
        idShop: Long,
        price: Int,
        currency: Long,
        idUser: Long?,
        dataLast: Long
    )

}