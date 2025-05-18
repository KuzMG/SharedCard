package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import com.example.sharedcard.database.entity.basket.BasketDao
import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.history.HistoryDao
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.statistic.data.CountWithMetricAndPrice
import com.example.sharedcard.database.entity.person.PersonDao
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.database.entity.purchase.PurchaseDao
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity.Companion.toHistory
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.ui.purchase.filter.enums.GROUPING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORTING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORT_MODE
import com.example.sharedcard.ui.statistic.data.ChartDate
import io.reactivex.Completable
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseManager @Inject constructor(
    private val purchaseDao: PurchaseDao,
    private val basketDao: BasketDao,
    private val historyDao: HistoryDao,
    private val personDao: PersonDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper,
) {
    fun getAllHistory() = historyDao.getAll(queryPreferences.personId)

    fun add(purchases: List<PurchaseEntity>) {
        purchaseDao.add(purchases)
    }

    fun add(purchase: PurchaseEntity) {
        purchaseDao.add(purchase)
    }


    fun delete(groupId: UUID, purchaseId: UUID): Completable =
        try {
            if (queryPreferences.groupId == groupId) {
                purchaseDao.delete(purchaseId)
                Completable.complete()
            } else {
                val password = personDao.getAccount(queryPreferences.personId).password
                stompHelper.deletePurchase(
                    purchaseId,
                    queryPreferences.personId,
                    groupId,
                    password
                )
            }
        } catch (e: Exception) {
            Completable.error(e)
        }


    fun add(
        productId: Int,
        description: String,
        count: Double,
        price: Double,
        groupId: UUID
    ): Completable {
        val purchase = PurchaseEntity(
            idProduct = productId,
            description = description,
            count = count,
            idPerson = queryPreferences.personId,
            idGroup = groupId,
            price = price,
            idCurrency = queryPreferences.currency
        )
        return try {
            if (groupId == queryPreferences.groupId) {
                purchaseDao.add(purchase)
                Completable.complete()
            } else {
                val password = personDao.getAccount(queryPreferences.personId).password
                stompHelper.sendPurchase(
                    purchase,
                    groupId,
                    queryPreferences.personId,
                    password
                )
            }
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    fun basketToHistory(
        groupId: UUID,
        basketId: UUID,
        shopId: Int,
        price: Double,
    ): Completable =
        try {
            val history = HistoryEntity(basketId, queryPreferences.personId, shopId, price)
            if (groupId == queryPreferences.groupId) {
                historyDao.add(history)
                Completable.complete()
            } else {
                val password = personDao.getAccount(queryPreferences.personId).password
                stompHelper.sendHistory(
                    history,
                    groupId,
                    queryPreferences.personId,
                    password
                )
            }
        } catch (e: Exception) {
            Completable.error(e)
        }

    fun addBasket(idPurchase: UUID, count: Double, groupId: UUID) = try {
        val basket = BasketEntity(idPurchase = idPurchase, count = count)
        if (groupId == queryPreferences.groupId) {
            basketDao.add(basket)
            Completable.complete()
        } else {
            val password = personDao.getAccount(queryPreferences.personId).password
            stompHelper.sendBasket(
                basket,
                groupId,
                queryPreferences.personId,
                password
            )
        }
    } catch (e: Exception) {
        Completable.error(Exception())
    }

    fun deleteBasket(basketId: UUID, groupId: UUID) = try {
        if (queryPreferences.groupId == groupId) {
            basketDao.delete(basketId)
            Completable.complete()
        } else {
            val password = personDao.getAccount(queryPreferences.personId).password
            stompHelper.deleteBasket(
                basketId,
                queryPreferences.personId,
                groupId,
                password
            )
        }
    } catch (e: Exception) {
        Completable.error(e)
    }

    fun getBaskets(purchaseId: UUID) = basketDao.getByPurchase(purchaseId)

    fun getCountBasket(purchaseId: UUID) = basketDao.getCount(purchaseId)


    fun purchaseToHistory(groupId: UUID, purchaseId: UUID) = try {
        val countHistory = historyDao.getCount(purchaseId)
        if (countHistory != 0) {
            if (queryPreferences.groupId == groupId) {
                purchaseDao.toHistory(purchaseId)
                basketDao.deleteAllByPurchase(purchaseId)
                Completable.complete()
            } else {
                val purchase = purchaseDao.findById(purchaseId)
                val boughtPurchase = purchase.toHistory()
                stompHelper.sendPurchaseToHistory(
                    boughtPurchase,
                    groupId
                )
            }
        } else {
            Completable.error(Exception())
        }
    } catch (e: Exception) {
        Completable.error(Exception())

    }

    fun getByFilter(
        groupingBy: GROUPING_BY,
        id: UUID,
        sortingBy: SORTING_BY,
        sortMode: SORT_MODE,
        excludeGroupsSet: MutableSet<UUID>,
        excludePersonsSet: MutableSet<UUID>,
        query: String
    ): LiveData<List<Purchase>> {
        val isGroup = when (groupingBy) {
            GROUPING_BY.GROUP -> true
            GROUPING_BY.PERSON -> false
        }
        val isAsc = when (sortMode) {
            SORT_MODE.ASC -> true
            SORT_MODE.DESC -> false
        }
        return when (sortingBy) {
            SORTING_BY.CATEGORY -> purchaseDao.getByGroupQueryOrderCategory(
                id,
                isGroup,
                "$query%",
                excludeGroupsSet,
                excludePersonsSet,
                isAsc
            )

            SORTING_BY.NAME -> purchaseDao.getByGroupQueryOrderName(
                id,
                isGroup,
                "$query%",
                excludeGroupsSet,
                excludePersonsSet,
                isAsc
            )

            SORTING_BY.DATE -> purchaseDao.getByGroupQueryOrderDate(
                id,
                isGroup,
                "$query%",
                excludeGroupsSet,
                excludePersonsSet,
                isAsc
            )
        }
    }

    fun getById(purchaseId: UUID) = purchaseDao.findByIdLiveData(purchaseId)
    fun getCountHistory(purchaseId: UUID): LiveData<Double> =
        historyDao.getCountPurchase(purchaseId)

    fun getAllByPersonId() = purchaseDao.getByPersonId(queryPreferences.personId)
    fun getAll() = purchaseDao.getAll(queryPreferences.personId)

    fun getCountPurchaseByPersonId(personId: UUID = queryPreferences.personId) =
        purchaseDao.getCountByPersonId(personId)
}