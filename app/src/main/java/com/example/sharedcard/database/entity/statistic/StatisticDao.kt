package com.example.sharedcard.database.entity.statistic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sharedcard.database.entity.basket.Basket
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.entity.statistic.data.CountAndPrice
import com.example.sharedcard.database.entity.statistic.data.CountWithMetricAndPrice
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.database.entity.statistic.data.CountChart
import java.util.UUID

@Dao
interface StatisticDao {
    @Query("select * from product order by popularity desc limit 1")
    fun getMostPopular(): LiveData<Product>
    @Query("select * from product where id = (select id_product from purchase where id_group=:groupId and creation_date>=:dateFirst and creation_date<=:dateLast group by id_product order by count(purchase.id) desc limit 1)")
    fun getMostPopular(groupId:UUID,dateFirst: Long, dateLast:Long): LiveData<Product>


    @Query("select count(purchase.id) from purchase where id_group=:groupId and creation_date>=:dateFirst and creation_date<=:dateLast group by id_product order by count(purchase.id) desc limit 1")
    fun getCountMostPopular(groupId:UUID,dateFirst: Long, dateLast:Long): LiveData<Int>



    @Query("select * from product " +
            "where id = " +
                "(select id_product from purchase " +
                    "where id = " +
                        "(select id_purchase from basket " +
                            "where id = " +
                                "(select id_basket from history " +
                                    "where history.id_person=:personId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
                                    "order by history.price desc limit 1))) ")
    fun getMostExpensiveProductByPersonId(personId: UUID,dateFirst: Long,dateLast:Long): LiveData<Product>


    @Query("select history.price, basket.count,currency.symbol, metric.name as metric from basket " +
            "   join history on history.id_basket = basket.id" +
            "   join purchase on purchase.id = basket.id_purchase" +
            "   join product on product.id = purchase.id_product " +
            "   join metric on product.id_metric = metric.id " +
            "   join currency on purchase.id_currency = currency.id" +
            "   where history.id_person=:personId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "   group by history.id_basket,history.price,basket.count,currency.symbol, metric.name " +
            "   order by history.price desc limit 1")
    fun getCountMostExpensiveProductByPersonId(personId: UUID,dateFirst: Long,dateLast:Long): LiveData<CountWithMetricAndPrice>


    @Query("select * from product where id = " +
            "(select id_basket from history " +
            "join basket on history.id_basket = id_basket " +
            "join purchase on basket.id_purchase = purchase.id  " +
            "where purchase.id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "order by history.price desc limit 1)")
    fun getMostExpensiveProductByGroupId(groupId: UUID,dateFirst: Long,dateLast:Long): LiveData<Product>

    @Query("select history.price, basket.count,currency.symbol, metric.name as metric from basket " +
            "   join history on history.id_basket = basket.id" +
            "   join purchase on purchase.id = basket.id_purchase" +
            "   join product on product.id = purchase.id_product " +
            "   join metric on product.id_metric = metric.id " +
            "   join currency on purchase.id_currency = currency.id" +
            "   where purchase.id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "   group by history.id_basket,history.price,basket.count,currency.symbol, metric.name " +
            "   order by history.price desc limit 1")
    fun getCountMostExpensiveProductByGroupId(groupId: UUID,dateFirst: Long,dateLast:Long): LiveData<CountWithMetricAndPrice>

    @Query("select count(*) as count, sum(history.price) as price, currency.symbol from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where history.id_person=:personId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast group by currency.symbol")
    fun getCountAndPriceByPersonId(personId: UUID,dateFirst: Long,dateLast: Long): LiveData<List<CountAndPrice>>
    @Query("select count(*) as count, sum(history.price) as price, currency.symbol from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where purchase.id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast group by currency.symbol")
    fun getCountAndPriceByGroupId(groupId: UUID,dateFirst: Long,dateLast: Long): LiveData<List<CountAndPrice>>


    @Query("select * from person " +
            "where id = " +
            "   (select id_person from purchase " +
            "       where id_group=:groupId and creation_date>=:dateFirst and creation_date<=:dateLast " +
            "       group by id_person " +
            "       order by count(purchase.id) desc limit 1)")
    fun getAddMostPurchasesPerson(groupId: UUID, dateFirst: Long, dateLast: Long): LiveData<PersonEntity>


    @Query("select * from person " +
            "where id = " +
            "   (select  history.id_person from history " +
            "       join basket on history.id_basket = basket.id " +
            "       join purchase on purchase.id = basket.id_purchase " +
            "           where id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "           group by history.id_person " +
            "           order by count(history.id_basket) desc limit 1)")
    fun getBuyMostPurchasesPerson(groupId: UUID, dateFirst: Long, dateLast: Long): LiveData<PersonEntity>


    @Query("select   strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') AS year," +
            "        strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') AS month," +
            "       strftime('%w', history.purchase_date / 1000, 'unixepoch','localtime') AS week," +
            "       strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') AS day," +
            "        count(history.id_basket) as count " +
            "from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where purchase.id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "group by year+month")
    fun getCountPurchaseChartByGroupYear(groupId: UUID, dateFirst: Long, dateLast: Long):LiveData<List<CountChart>>

    @Query("select   strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') AS year," +
            "        strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') AS month," +
            "       strftime('%w', history.purchase_date / 1000, 'unixepoch','localtime') AS week," +
            "       strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') AS day," +
            "        count(history.id_basket) as count " +
            "from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where purchase.id_group=:groupId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "group by year+month+day")
    fun getCountPurchaseChartByGroupDay(groupId: UUID, dateFirst: Long, dateLast: Long):LiveData<List<CountChart>>


    @Query("select   strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') AS year," +
            "        strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') AS month," +
            "       strftime('%w', history.purchase_date / 1000, 'unixepoch','localtime') AS week," +
            "       strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') AS day," +
            "        count(history.id_basket) as count " +
            "from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where history.id_person=:personId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "group by year+month")
    fun getCountPurchaseChartByPersonYear(personId: UUID, dateFirst: Long, dateLast: Long):LiveData<List<CountChart>>

    @Query("select   strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') AS year," +
            "        strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') AS month," +
            "       strftime('%w', history.purchase_date / 1000, 'unixepoch','localtime') AS week," +
            "       strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') AS day," +
            "        count(history.id_basket) as count " +
            "from history " +
            "join basket on basket.id=history.id_basket " +
            "join purchase on purchase.id=basket.id_purchase " +
            "join currency on purchase.id_currency=currency.id " +
            "where history.id_person=:personId and history.purchase_date>=:dateFirst and history.purchase_date<=:dateLast " +
            "group by year+month+day")
    fun getCountPurchaseChartByPersonDay(personId: UUID, dateFirst: Long, dateLast: Long):LiveData<List<CountChart>>

    @Query("select history.id_person,id_purchase,history.purchase_date, sum(history.price) as price, sum(basket.count) as count, id_shop from history " +
            "    join basket on basket.id = history.id_basket " +
            "    join purchase on purchase.id = basket.id_purchase " +
            "        where " +
            "           history.id_person =:personId and " +
            "           strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') =:year and " +
            "           strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') =:month" +
            "        group by id_purchase,history.purchase_date,history.id_shop, history.id_person " +
            "        order by history.purchase_date ASC")
    fun getPurchasesYearsPersonId(personId:UUID,year: String, month: String): LiveData<List<History>>

    @Query("select history.id_person,id_purchase,history.purchase_date, sum(history.price) as price, sum(basket.count) as count, id_shop from history " +
            "    join basket on basket.id = history.id_basket " +
            "    join purchase on purchase.id = basket.id_purchase " +
            "        where " +
            "           history.id_person =:personId and " +
            "           strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') =:year and " +
            "           strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') =:month and" +
            "           strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') =:day and " +
            "            purchase.is_bought = 1" +
            "        group by id_purchase,history.purchase_date,history.id_shop, history.id_person " +
            "        order by history.purchase_date ASC")
    fun getPurchasesMonthsPersonId(personId:UUID,year: String, month: String,day: String): LiveData<List<History>>


    @Query("select history.id_person,id_purchase,history.purchase_date, sum(history.price) as price, sum(basket.count) as count, id_shop from history " +
            "    join basket on basket.id = history.id_basket " +
            "    join purchase on purchase.id = basket.id_purchase " +
            "        where " +
            "           purchase.id_group =:groupId and " +
            "           strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') =:year and " +
            "           strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') =:month and " +
            "            purchase.is_bought = 1" +
            "        group by id_purchase,history.purchase_date,history.id_shop, history.id_person " +
            "        order by history.purchase_date ASC")
    fun getPurchasesYearsGroupId(groupId:UUID,year: String, month: String): LiveData<List<History>>

    @Query("select history.id_person,id_purchase,history.purchase_date, sum(history.price) as price, sum(basket.count) as count, id_shop from history " +
            "    join basket on basket.id = history.id_basket " +
            "    join purchase on purchase.id = basket.id_purchase " +
            "        where " +
            "           purchase.id_group =:groupId and " +
            "           strftime('%Y', history.purchase_date / 1000, 'unixepoch','localtime') =:year and " +
            "           strftime('%m', history.purchase_date / 1000, 'unixepoch','localtime') =:month and" +
            "           strftime('%d', history.purchase_date / 1000, 'unixepoch','localtime') =:day and" +
            "           purchase.is_bought = 1" +
            "        group by id_purchase,history.purchase_date,history.id_shop, history.id_person " +
            "        order by history.purchase_date ASC")
    fun getPurchasesMonthsGroupId(groupId:UUID,year: String, month: String,day: String): LiveData<List<History>>

}