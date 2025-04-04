package com.example.sharedcard.database.entity.target

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
interface TargetDao {
    @Query(
        "select target.id,target.name,c.name as category,price_first as price,cur.symbol as currency,u.name as user,u.pic as picUser,date_first as dateFirst from target " +
                "join category as c on id_category = c.id " +
                "join currency as cur on id_currency_first = cur.id " +
                "join user as u on id_creator = u.id " +
                "where target.id_group = :id and target.status = 0 " +
                "order by date_first desc"
    )
    fun getAllForCheck(id: UUID): LiveData<List<Target>>

    @Query(
        "select target.id,target.name,c.name as category,price_first as price,cur.symbol as currency,u.name as user,u.pic as picUser,date_first as dateFirst from target " +
                "join category as c on id_category = c.id " +
                "join currency as cur on id_currency_first = cur.id " +
                "join user as u on id_creator = u.id " +
                "where target.id_group = :id and target.status = 0 and target.name like :query " +
                "order by date_first desc"
    )
    fun getAllForCheckQuery(id: UUID, query: String): LiveData<List<Target>>

    @Query(
        "select target.id,target.name,c.name as category,price_first,curF.symbol as currencyFirst,price_last,curL.symbol as currencyLast,uf.name as userFirst,uf.pic as picUserFirst,ul.name as userLast,ul.pic as picUserLast,sh.name as shop,date_last as dateLast from target " +
                "join category as c on id_category = c.id " +
                "join shop as sh on id_shop = sh.id " +
                "join currency as curF on id_currency_first = curF.id " +
                "join currency as curL on id_currency_last = curL.id " +
                "join user as uf on id_creator = uf.id " +
                "join user as ul on id_buyer = ul.id " +
                "where target.id_group = :id and target.status = 2 " +
                "order by date_first desc"
    )
    fun getAllForHistory(id: UUID): LiveData<List<TargetHistory>>

    @Query(
        "select target.id,target.name,c.name as category,price_first,curF.symbol as currencyFirst,price_last,curL.symbol as currencyLast,uf.name as userFirst,uf.pic as picUserFirst,ul.name as userLast,ul.pic as picUserLast,sh.name as shop,date_last as dateLast from target " +
                "join category as c on id_category = c.id " +
                "join shop as sh on id_shop = sh.id " +
                "join currency as curF on id_currency_first = curF.id " +
                "join currency as curL on id_currency_last = curL.id " +
                "join user as uf on id_creator = uf.id " +
                "join user as ul on id_buyer = ul.id " +
                "where target.id_group = :id and target.status = 2 and target.name like :query " +
                "order by date_first desc"
    )
    fun getAllForHistoryQuery(id: UUID, query: String): LiveData<List<TargetHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(target: List<TargetEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(target: TargetEntity): Long

    @Update
    fun update(target: TargetEntity): Int

    @Transaction
    fun insertOrUpdate(target: TargetEntity) {
        val id = insert(target)
        if (id ==-1L)
            update(target)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(target: TargetEntity)

    @Query("update target set status = :status where id=:id")
    fun uprateStatus(id: UUID, status: Int)

    @Query("delete from target where id = :id")
    fun delete(id: UUID)

    @Query(
        "update target set " +
                "status = 1, " +
                "id_shop = :idShop, " +
                "price_last = :price, " +
                "id_currency_last = :currency, " +
                "id_buyer = :idUser, " +
                "date_last = :dataLast " +
                "where id = :id"
    )
    fun toHistory(
        id: UUID,
        idShop: Int,
        price: Int,
        currency: Int,
        idUser: UUID,
        dataLast: Long
    )

    @Query("select * from 'target' where id =:id")
    fun findById(id: UUID): TargetEntity
}