package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.target.TargetDao
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TargetRepository @Inject constructor(private val targetDao: TargetDao) {


    fun getAllCheck(
        id: UUID
    ) = targetDao.getAllForCheck(id)


    fun getAllQuery(
        id: UUID,
        query: String
    ) = targetDao.getAllForCheckQuery(id, "$query%")

    fun getAllHistory(
        id: UUID
    ) = targetDao.getAllForHistory(id)

    fun getAllHistoryQuery(
        id: UUID,
        query: String
    ) = targetDao.getAllForHistoryQuery(id, "$query%")

    fun add(target: TargetEntity) {
        targetDao.add(target)
    }

    fun setStatus(id: UUID, status: Int) {
        targetDao.uprateStatus(id, status)
    }

    fun delete(id: UUID) {
        targetDao.delete(id)
    }

    fun toHistory(id: UUID, idShop: Long, price: Int, idCurrency: Long, idUser: UUID) {
        targetDao.toHistory(id, idShop, price, idCurrency, idUser, Date().time)
    }
}