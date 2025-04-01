package com.example.sharedcard.database.entity.purchase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import java.util.Date
import java.util.UUID

@Dao
interface PurchaseDao {
//    @Query(
//        "select purchase.id, " +
//                "pr.name as name," +
//                "c.name as category," +
//                "purchase.count," +
//                "m.name as metric," +
//                "p.name as person," +
//                "p.pic as personPicId," +
//                "description," +
//                "purchase.creation_date," +
//                "pr.calories," +
//                "pr.carb as carb," +
//                "pr.fat as fat," +
//                "pr.protein as protein from purchase " +
//                "join product as pr on purchase.id_product = pr.id " +
//                "join category_product as c on pr.id_category_product = c.id " +
//                "join person as p on p.id = purchase.id_person " +
//                "join metric as m on m.id = id_metric " +
//                "where id_group = :groupId and is_bought = 0 " +
//                "order by name desc"
//    )
//    fun getByGroup(groupId: UUID): LiveData<List<Purchase>>

    @Query("select * from `group` where (select count(purchase.id) from purchase where purchase.id_group = `group`.id)>0")
    fun getAllGroups() : LiveData<List<PurchaseGroup>>
//    @Query(
//        "select purchase.id, " +
//                "pr.name as name," +
//                "c.name as category," +
//                "purchase.count," +
//                "m.name as metric," +
//                "p.name as person," +
//                "p.pic as personPicId," +
//                "description," +
//                "purchase.creation_date," +
//                "pr.calories," +
//                "pr.carb as carb," +
//                "pr.fat as fat," +
//                "pr.protein as protein from purchase " +
//                "join product as pr on purchase.id_product = pr.id " +
//                "join category_product as c on pr.id_category_product = c.id " +
//                "join person as p on p.id = purchase.id_person " +
//                "join metric as m on m.id = id_metric " +
//                "where id_group = :groupId and is_bought = 0 and pr.name like :query " +
//                "order by name desc"
//    )
//    fun getByGroupQuery(groupId: UUID, query: String): LiveData<List<Purchase>>


    @Query("delete from purchase where id = :id")
    fun delete(id: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(purchases: List<PurchaseEntity>)

    @Insert()
    fun add(purchases: PurchaseEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(purchase: PurchaseEntity): Long

    @Update
    fun update(purchase: PurchaseEntity): Int

    @Transaction
    fun insertOrUpdate(purchase: PurchaseEntity) {
        val id = insert(purchase)
        if (id ==-1L)
            update(purchase)
    }



    @Query("select * from purchase where id = :id")
    fun findById(id: UUID): PurchaseEntity
    @Query("select * from purchase where id_group = :groupId and purchase_date is NULL")
    fun getByGroup(groupId: UUID): LiveData<List<Purchase>>


    @Query("select * from purchase where " +
            "(CASE " +
            "WHEN :isGroup = 1 THEN purchase.id_group " +
            "WHEN :isGroup = 0 THEN purchase.id_person " +
            "END) = :id and " +
            "(select name from product where product.id = purchase.id_product) like :query " +
            "and purchase_date is NULL " +
            "and id_group not in (:excludeGroupsSet) " +
            "and id_person not in (:excludePersonsSet) " +
            "order by  CASE WHEN :isAsc = 1 THEN creation_date END ASC, \n" +
            "        CASE WHEN :isAsc = 0 THEN creation_date END DESC")
    fun getByGroupQueryOrderDate(
        id: UUID,
        isGroup: Boolean,
        query: String,
        excludeGroupsSet: Set<UUID>,
        excludePersonsSet: Set<UUID>,
        isAsc: Boolean
    ): LiveData<List<Purchase>>

    @Query("select * from purchase where " +
            "(CASE " +
            "WHEN :isGroup = 1 THEN purchase.id_group " +
            "WHEN :isGroup = 0 THEN purchase.id_person " +
            "END) = :id and " +
            "(select name from product where product.id = purchase.id_product) like :query " +
            "and purchase_date is NULL " +
            "and id_group not in (:excludeGroupsSet) " +
            "and id_person not in (:excludePersonsSet) " +
            "order by  CASE WHEN :isAsc = 1 THEN (select id_category_product from product where product.id = id_product) END ASC, \n" +
            "        CASE WHEN :isAsc = 0 THEN (select id_category_product from product where product.id = id_product) END DESC")
    fun getByGroupQueryOrderCategory(
        id: UUID,
        isGroup: Boolean,
        query: String,
        excludeGroupsSet: Set<UUID>,
        excludePersonsSet: Set<UUID>,
        isAsc: Boolean
    ): LiveData<List<Purchase>>
    @Query("select * from purchase where " +
            "(CASE " +
            "WHEN :isGroup = 1 THEN purchase.id_group " +
            "WHEN :isGroup = 0 THEN purchase.id_person " +
            "END) = :id and " +
            "(select name from product where product.id = purchase.id_product) like :query " +
            "and purchase_date is NULL " +
            "and id_group not in (:excludeGroupsSet) " +
            "and id_person not in (:excludePersonsSet) " +
            "order by  CASE WHEN :isAsc = 1 THEN (select product.name from product where product.id = id_product) END ASC, \n" +
            "        CASE WHEN :isAsc = 0 THEN (select product.name from product where product.id = id_product) END DESC")
    fun getByGroupQueryOrderName(
        id: UUID,
        isGroup: Boolean,
        query: String,
        excludeGroupsSet: Set<UUID>,
        excludePersonsSet: Set<UUID>,
        isAsc: Boolean
    ): LiveData<List<Purchase>>
    @Query("select count(*) from purchase where id_group = :groupId ")
    fun getCount(groupId: UUID): LiveData<Int>
    @Query("select count(*) from purchase where id_group = :groupId and (select name from product where product.id=id_product) like :query ")
    fun getCountQuery(groupId: UUID, query: String): LiveData<Int>
    @Query("update purchase set purchase_date = :date where id=:purchaseId")
    fun toHistory(purchaseId: UUID,date: Long = Date().time)

    @RawQuery
    fun get(
        query: SupportSQLiteQuery
    ): LiveData<List<Purchase>>
    @Query("select * from purchase where id = :purchaseId")
    fun findByIdLiveData(purchaseId: UUID): LiveData<Purchase>

}