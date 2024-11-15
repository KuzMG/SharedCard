package com.example.sharedcard.database.entity.check

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.UUID

@Dao
interface CheckDao {
    @Query(
        "select `check`.id, pr.name as name,c.name as category,u.name as user,u.pic as picUser,m.name as metric,count,date_first,`check`.status as status,description,pr.calories,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                "join product as pr on `check`.id_product = pr.id " +
                "join category as c on pr.id_category = c.id " +
                "join user as u on u.id = id_creator " +
                "join metric as m on m.id = id_metric " +
                "where id_group = :id and `check`.status != 2 " +
                "order by `check`.status, date_first desc"
    )
    fun getAllForCheck(id: UUID): LiveData<List<Check>>

    @Query(
        "select `check`.id, pr.name as name,c.name as category,u.name as user,u.pic as picUser,m.name as metric,count,date_first,`check`.status as status,description,pr.calories,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                "join product as pr on `check`.id_product = pr.id " +
                "join category as c on pr.id_category = c.id " +
                "join user as u on u.id = id_creator " +
                "join metric as m on m.id = id_metric " +
                "where id_group = :id and `check`.status != 2 and pr.name like :query " +
                "order by `check`.status, date_first desc"
    )
    fun getAllForCheckQuery(id: UUID, query: String): LiveData<List<Check>>

    @Query(
        "select `check`.id ,pr.name as name,c.name as category,uf.name as userFirst,ul.name as userLast,sh.name as shop,m.name as metric,count,price,cur.symbol as currency,date_last,uf.pic as picUserFirst,ul.pic as picUserLast,pr.calories,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                "join product as pr on `check`.id_product = pr.id " +
                "join shop as sh on sh.id = id_shop " +
                "join category as c on pr.id_category = c.id " +
                "join user as ul on ul.id = id_buyer " +
                "join user as uf on uf.id = id_creator " +
                "join metric as m on m.id = id_metric " +
                "join currency as cur on cur.id = id_currency " +
                "where id_group = :id and `check`.status = 2 " +
                "order by date_last desc"
    )
    fun getAllForHistory(id: UUID): LiveData<List<CheckHistory>>

    @Query(
        "select `check`.id ,pr.name as name,c.name as category,uf.name as userFirst,ul.name as userLast,sh.name as shop,m.name as metric,count,price,cur.symbol as currency,date_last,uf.pic as picUserFirst,ul.pic as picUserLast,pr.calories,pr.carb as carb,pr.fat as fat,pr.protein as protein from `check` " +
                "join product as pr on `check`.id_product = pr.id " +
                "join shop as sh on sh.id = id_shop " +
                "join category as c on pr.id_category = c.id " +
                "join user as ul on ul.id = id_buyer " +
                "join user as uf on uf.id = id_creator " +
                "join metric as m on m.id = id_metric " +
                "join currency as cur on cur.id = id_currency " +
                "where id_group = :id and `check`.status = 2 and pr.name like :query " +
                "order by date_last desc"
    )
    fun getAllForHistoryQuery(id: UUID, query: String): LiveData<List<CheckHistory>>

    @Query("delete from `check` where id = :id")
    fun delete(id: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(checks: List<CheckEntity>)

    @Insert()
    fun add(checks: CheckEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(check: CheckEntity): Long

    @Update
    fun update(check: CheckEntity): Int

    @Transaction
    fun insertOrUpdate(check: CheckEntity) {
        val id = insert(check)
        if (id ==-1L)
            update(check)
    }
    @Query("update `check` set status = :status where id=:id")
    fun uprateStatus(id: UUID, status: Int)

    @Query(
        "update `check` set " +
                "status = 2, " +
                "id_shop = :idShop, " +
                "price = :price, " +
                "id_currency = :currency, " +
                "id_buyer = :idUser, " +
                "date_last = :dataLast " +
                "where id=:id"
    )
    fun toHistory(
        id: UUID,
        idShop: Int,
        price: Int,
        currency: Int,
        idUser: UUID,
        dataLast: Long
    )

    @Query("select * from 'check' where id = :id")
    fun findById(id: UUID): CheckEntity

}