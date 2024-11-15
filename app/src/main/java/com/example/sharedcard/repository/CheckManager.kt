package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.check.CheckDao
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.user.UserDao
import com.example.sharedcard.service.stomp.StompHelper
import io.reactivex.Completable
import java.net.ConnectException
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckManager @Inject constructor(
    private val checkDao: CheckDao,
    private val userDao: UserDao,
    private val queryPreferences: QueryPreferences,
    private val stompHelper: StompHelper,
) {


    fun getAllCheck() = checkDao.getAllForCheck(queryPreferences.groupId)


    fun getAllQuery(query: String) =
        checkDao.getAllForCheckQuery(queryPreferences.groupId, "$query%")

    fun getAllHistory() = checkDao.getAllForHistory(queryPreferences.groupId)


    fun getAllHistoryQuery(
        query: String
    ) = checkDao.getAllForHistoryQuery(queryPreferences.groupId, "$query%")


    fun add(products: List<CheckEntity>) {
        checkDao.add(products)
    }

    fun add(product: CheckEntity) {
        checkDao.add(product)
    }

    fun setStatus(id: UUID, status: Int) {
        checkDao.uprateStatus(id, status)
    }

    fun delete(isInternetConnection: Boolean, idCheck: UUID): Completable =
        if (queryPreferences.isLocal) {
            checkDao.delete(idCheck)
            Completable.complete()
        } else if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.deleteCheck(
                idCheck,
                queryPreferences.userId,
                queryPreferences.groupId,
                userPassword
            )
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }


    fun add(
        isInternetConnection: Boolean,
        idProduct: Int,
        description: String,
        idMetric: Int,
        count: Int
    ): Completable {
        val check = CheckEntity(
            idProduct = idProduct,
            description = description,
            idMetric = idMetric,
            count = count,
            idCreator = queryPreferences.userId,
            idGroup = queryPreferences.groupId
        )
        return if (queryPreferences.isLocal) {
            checkDao.add(check)
            Completable.complete()
        } else if (isInternetConnection) {
            val userPassword = userDao.getAccount(queryPreferences.userId).password
            stompHelper.sendCheck(
                check,
                queryPreferences.groupId,
                queryPreferences.userId,
                userPassword
            )
        } else {
            Completable.error(ConnectException("Нет подключения к интернету!"))
        }


    }

    fun toHistory(
        isInternetConnection: Boolean,
        id: UUID,
        idShop: Int,
        price: Int,
        idCurrency: Int
    ): Completable = if (queryPreferences.isLocal) {
        checkDao.toHistory(id, idShop, price, idCurrency, queryPreferences.userId, Date().time)
        Completable.complete()
    } else if (isInternetConnection) {
        val _check = checkDao.findById(id)
        val check = CheckEntity(
            _check.id,
            _check.idProduct,
            _check.description,
            CheckEntity.HISTORY,
            _check.idGroup,
            idShop,
            price,
            _check.count,
            _check.idMetric,
            idCurrency,
            _check.idCreator,
            queryPreferences.userId,
            _check.dataFirst,
            Date().time

        )
        val userPassword = userDao.getAccount(queryPreferences.userId).password
        stompHelper.sendCheck(
            check,
            queryPreferences.groupId,
            queryPreferences.userId,
            userPassword
        )
    } else {
        Completable.error(ConnectException("Нет подключения к интернету!"))
    }

}