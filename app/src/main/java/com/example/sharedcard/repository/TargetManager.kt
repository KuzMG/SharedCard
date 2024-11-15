package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.target.TargetDao
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.service.stomp.StompHelper
import com.project.shared_card.database.dao.target.TargetEntity
import io.reactivex.Completable
import java.net.ConnectException
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TargetManager @Inject constructor(
    private val targetDao: TargetDao,
    private val userDao: UserDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper
) {


    fun getAllCheck() = targetDao.getAllForCheck(queryPreferences.groupId)


    fun getAllQuery(query: String) =
        targetDao.getAllForCheckQuery(queryPreferences.groupId, "$query%")

    fun getAllHistory() = targetDao.getAllForHistory(queryPreferences.groupId)

    fun getAllHistoryQuery(query: String) =
        targetDao.getAllForHistoryQuery(queryPreferences.groupId, "$query%")

    fun add(target: List<TargetEntity>) {
        targetDao.add(target)
    }

    fun add(target: TargetEntity) {
        targetDao.add(target)
    }

    fun setStatus(id: UUID, status: Int) {
        targetDao.uprateStatus(id, status)
    }

    fun delete(isInternetConnection: Boolean, idTarget: UUID): Completable =
        if (queryPreferences.isLocal) {
            targetDao.delete(idTarget)
            Completable.complete()
        } else if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.deleteTarget(
                idTarget,
                queryPreferences.userId,
                queryPreferences.groupId,
                userPassword
            )
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }


    fun toHistory(
        isInternetConnection: Boolean,
        id: UUID,
        idShop: Int,
        price: Int,
        idCurrency: Int
    ): Completable =
        if (queryPreferences.isLocal) {
            targetDao.toHistory(id, idShop, price, idCurrency, queryPreferences.userId, Date().time)
            Completable.complete()
        } else if (isInternetConnection) {
            val _target = targetDao.findById(id)
            val target = TargetEntity(
                _target.id,
                _target.name,
                TargetEntity.HISTORY,
                _target.idGroup,
                _target.idCategory,
                idShop,
                _target.priceFirst,
                price,
                _target.idCurrencyFirst,
                idCurrency,
                _target.idCreator,
                queryPreferences.userId,
                _target.dateFirst,
                Date().time
            )
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.sendTarget(
                target,
                queryPreferences.groupId,
                queryPreferences.userId,
                userPassword
            )
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }

    fun add(
        isInternetConnection: Boolean,
        name: String,
        price: Int,
        currency: Int,
        category: Int
    ): Completable {
        val target = TargetEntity(
            name = name,
            priceFirst = price,
            idCurrencyFirst = currency,
            idCategory = category,
            idGroup = queryPreferences.groupId,
            idCreator = queryPreferences.userId
        )
        return if (queryPreferences.isLocal) {
            targetDao.add(target)
            Completable.complete()
        } else if (isInternetConnection) {
            val password = userDao.getAccount(queryPreferences.userId).password
            stompHelper.sendTarget(
                target,
                queryPreferences.groupId,
                queryPreferences.userId,
                password
            )
        } else {
            Completable.error(ConnectException("Нет подключения к интернету"))
        }
    }
}


