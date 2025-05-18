package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.database.entity.statistic.StatisticDao
import com.example.sharedcard.database.entity.statistic.data.CountChart
import com.example.sharedcard.database.entity.statistic.data.CountWithMetricAndPrice
import com.example.sharedcard.ui.statistic.data.ChartDate
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticRepository @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val statisticDao: StatisticDao
) {

    fun getCountAndPriceHistoryByPersonId(
        personId: UUID = queryPreferences.personId,
        dateFirst: Long,
        dateLast: Long
    ) = statisticDao.getCountAndPriceByPersonId(personId, dateFirst, dateLast)

    fun getCountAndPriceHistoryByGroupId(groupId: UUID, dateFirst: Long, dateLast: Long) =
        statisticDao.getCountAndPriceByGroupId(groupId, dateFirst, dateLast)


    fun getMostExpensiveProduct(dateFirst: Long, dateLast: Long): LiveData<Product> =
        statisticDao.getMostExpensiveProductByPersonId(
            queryPreferences.personId,
            dateFirst,
            dateLast
        )

    fun getMostExpensiveProduct(groupId: UUID, dateFirst: Long, dateLast: Long): LiveData<Product> =
        statisticDao.getMostExpensiveProductByGroupId(groupId, dateFirst, dateLast)

    fun getCountMostExpensiveProduct(
        dateFirst: Long,
        dateLast: Long
    ): LiveData<CountWithMetricAndPrice> =
        statisticDao.getCountMostExpensiveProductByPersonId(
            queryPreferences.personId,
            dateFirst,
            dateLast
        )

    fun getCountMostExpensiveProduct(
        groupId: UUID,
        dateFirst: Long,
        dateLast: Long
    ): LiveData<CountWithMetricAndPrice> =
        statisticDao.getCountMostExpensiveProductByGroupId(groupId, dateFirst, dateLast)

    fun getMostPopularProduct(): LiveData<Product> = statisticDao.getMostPopular()

    fun getMostPopularProduct(groupId: UUID, dateFirst: Long, dateLast: Long): LiveData<Product> =
        statisticDao.getMostPopular(groupId, dateFirst, dateLast)

    fun getCountMostPopularProduct(groupId: UUID, dateFirst: Long, dateLast: Long): LiveData<Int> =
        statisticDao.getCountMostPopular(groupId, dateFirst, dateLast)

    fun getAddMostPurchasesPerson(groupId: UUID, dateFirst: Long, dateLast: Long) =
        statisticDao.getAddMostPurchasesPerson(groupId, dateFirst, dateLast)

    fun getBuyMostPurchasesPerson(groupId: UUID, dateFirst: Long, dateLast: Long) =
        statisticDao.getBuyMostPurchasesPerson(groupId, dateFirst, dateLast)


    fun getCountPurchaseChartByGroup(
        groupId: UUID,
        dateFirst: Long,
        dateLast: Long,
        datePresentation: ChartDate
    ) = if (datePresentation == ChartDate.YEAR) {
        statisticDao.getCountPurchaseChartByGroupYear(groupId, dateFirst, dateLast)
    } else {
        statisticDao.getCountPurchaseChartByGroupDay(groupId, dateFirst, dateLast)
    }


    fun getCountPurchaseChartByPerson(
        dateFirst: Long,
        dateLast: Long,
        datePresentation: ChartDate
    ) = if (datePresentation == ChartDate.YEAR) {
        statisticDao.getCountPurchaseChartByPersonYear(
            queryPreferences.personId,
            dateFirst,
            dateLast
        )
    } else {
        statisticDao.getCountPurchaseChartByPersonDay(
            queryPreferences.personId,
            dateFirst,
            dateLast
        )
    }

    fun getPurchasesPersonId(countChart: CountChart, chartPresentation: ChartDate) =
        when (chartPresentation) {
            ChartDate.YEAR -> statisticDao.getPurchasesYearsPersonId(
                queryPreferences.personId,
                countChart.year,
                countChart.month
            )
            else -> statisticDao.getPurchasesMonthsPersonId(
                queryPreferences.personId,
                countChart.year,
                countChart.month,
                countChart.day
            )
        }

    fun getPurchasesGroupId(groupId:UUID,countChart: CountChart, chartPresentation: ChartDate) =
        when (chartPresentation) {
            ChartDate.YEAR -> statisticDao.getPurchasesYearsGroupId(
                groupId,
                countChart.year,
                countChart.month
            )
            else -> statisticDao.getPurchasesMonthsGroupId(
                groupId,
                countChart.year,
                countChart.month,
                countChart.day
            )
        }


}