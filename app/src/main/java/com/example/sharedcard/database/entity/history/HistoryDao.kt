package com.example.sharedcard.database.entity.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query(
        "select name, category, shop, buyer,count,metric,last_price as lastPrice,lastCurrency,date_last as dateLast from " +
                "(select product.name as name,c.name as category,sp.name as shop,uc.name as creator,ub.name as buyer, count, m.name as metric,price as last_price,cr.name as lastCurrency,date_first,date_last from product " +
                "join category_product as c on id_category = c.id " +
                "join metric as m on id_metric = m.id " +
                "join currency as cr on id_currency = cr.id " +
                "join shop_product as sp on id_shop = sp.id " +
                "left join user as uc on id_user_creator = uc.id_user " +
                "left join user as ub on id_user_buyer = ub.id_user " +
                "where status ==2 and id_group=:id" +
                " union " +
                "select target.name as name,ct.name as category,st.name as shop,uc.name as creator,ub.name as buyer, first_price as count,crf.name as metric,last_price, crl.name as lastCurrency,date_first,date_last from target " +
                "join category_target as ct on id_category = ct.id " +
                "join currency as crf on id_currency_first = crf.id " +
                "join currency as crl on id_currency_last = crl.id " +
                "join shop_target as st on id_shop = st.id " +
                "left join user as uc on id_user_creator = uc.id_user " +
                "left join user as ub on id_user_buyer = ub.id_user " +
                "where status=2 and id_group=:id)" +
                "order By date_last desc"
    )
    fun getAll(id: Long): LiveData<List<History>>

    @Query(
        "select name, category, shop, buyer,count,metric,last_price as lastPrice,lastCurrency,date_last as dateLast from " +
                "(select product.name as name,c.name as category,sp.name as shop,uc.name as creator,ub.name as buyer, count, m.name as metric,price as last_price,cr.name as lastCurrency,date_first,date_last from product " +
                "join category_product as c on id_category = c.id " +
                "join metric as m on id_metric = m.id " +
                "join currency as cr on id_currency = cr.id " +
                "join shop_product as sp on id_shop = sp.id " +
                "left join user as uc on id_user_creator = uc.id_user " +
                "left join user as ub on id_user_buyer = ub.id_user " +
                "where status ==2 and id_group=:id" +
                " union " +
                "select target.name as name,c.name as category,sp.name shop,uc.name as creator,ub.name as buyer, first_price as count,crf.name as metric,last_price, crl.name as lastCurrency,date_first,date_last from target " +
                "join category_target as c on id_category = c.id " +
                "join currency as crf on id_currency_first = crf.id " +
                "join currency as crl on id_currency_last = crl.id " +
                "join shop_target as sp on id_shop = sp.id " +
                "left join user as uc on id_user_creator = uc.id_user " +
                "left join user as ub on id_user_buyer = ub.id_user " +
                "where status=2 and id_group=:id)" +
                "where name like :query " +
                "order By date_last desc "
    )
    fun getAllForQuery(id: Long, query: String): LiveData<List<History>>
}