package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.check.CheckDao
import com.example.sharedcard.database.entity.check.CheckEntity
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckRepository @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val checkDao: CheckDao
) {


    fun getAllCheck() = checkDao.getAllForCheck(queryPreferences.groupId)


    fun getAllQuery(query: String) =
        checkDao.getAllForCheckQuery(queryPreferences.groupId, "$query%")

    fun getAllHistory() = checkDao.getAllForHistory(queryPreferences.groupId)


    fun getAllHistoryQuery(
        query: String
    ) = checkDao.getAllForHistoryQuery(queryPreferences.groupId, "$query%")


    fun add(product: CheckEntity) {
        checkDao.add(product)
    }

    fun setStatus(id: UUID, status: Int) {
        checkDao.uprateStatus(id, status)
    }

    fun delete(id: UUID) {
        checkDao.delete(id)
    }

    fun toHistory(id: UUID, idShop: Long, price: Int, idCurrency: Long) {
        checkDao.toHistory(id, idShop, price, idCurrency, queryPreferences.userId, Date().time)
    }
}