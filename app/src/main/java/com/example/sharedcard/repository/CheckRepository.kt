package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.check.CheckDao
import com.example.sharedcard.database.entity.check.CheckEntity
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckRepository @Inject constructor(private val checkDao: CheckDao) {


    fun getAllCheck(
        id: UUID
    ) = checkDao.getAllForCheck(id)


    fun getAllQuery(
        id: UUID,
        query: String
    ) = checkDao.getAllForCheckQuery(id, "$query%")

    fun getAllHistory(
        id: UUID
    ) = checkDao.getAllForHistory(id)


    fun getAllHistoryQuery(
        id: UUID,
        query: String
    ) = checkDao.getAllForHistoryQuery(id, "$query%")


    fun add(product: CheckEntity) {
        checkDao.add(product)
    }

    fun setStatus(id: UUID, status: Int) {
        checkDao.uprateStatus(id, status)
    }

    fun delete(id: UUID) {
        checkDao.delete(id)
    }

    fun toHistory(id: UUID, idShop: Long, price: Int, idCurrency: Long, idUser: UUID) {
        checkDao.toHistory(id, idShop, price, idCurrency, idUser, Date().time)
    }
}