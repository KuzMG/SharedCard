package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.target.TargetDao
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TargetRepository @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val targetDao: TargetDao
) {


    fun getAllCheck() = targetDao.getAllForCheck(queryPreferences.groupId)


    fun getAllQuery(query: String) =
        targetDao.getAllForCheckQuery(queryPreferences.groupId, "$query%")

    fun getAllHistory() = targetDao.getAllForHistory(queryPreferences.groupId)

    fun getAllHistoryQuery(query: String) =
        targetDao.getAllForHistoryQuery(queryPreferences.groupId, "$query%")

    fun add(target: TargetEntity) {
        targetDao.add(target)
    }

    fun setStatus(id: UUID, status: Int) {
        targetDao.uprateStatus(id, status)
    }

    fun delete(id: UUID) {
        targetDao.delete(id)
    }

    fun toHistory(id: UUID, idShop: Long, price: Int, idCurrency: Long) {
        targetDao.toHistory(id, idShop, price, idCurrency, queryPreferences.userId, Date().time)
    }
}