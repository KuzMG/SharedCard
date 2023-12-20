package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.project.shared_card.database.dao.target.TargetEntity
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TargetRepository private constructor(database: AppDatabase) {
    companion object {
        private var nTargetRepository: TargetRepository? = null
        fun getInstance(database: AppDatabase): TargetRepository {
            if (nTargetRepository == null) {
                nTargetRepository = TargetRepository(database)
            }
            return nTargetRepository!!
        }
    }

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val targetDao = database.targetDao()

    fun getAllCheck(
        id: Long
    ) = targetDao.getAllForCheck(id)


    fun getAllQuery(
        id: Long,
        query: String
    ) = targetDao.getAllForCheckQuery(id, "$query%")

    fun getAllHistory(
        id: Long
    ) = targetDao.getAllForHistory(id)

    fun getAllHistoryQuery(
        id: Long,
        query: String
    ) = targetDao.getAllForHistoryQuery(id, "$query%")

    fun add(target: TargetEntity) {
        executor.execute {
            targetDao.add(target)
        }
    }

    fun setStatus(id: Long, status: Int) {
        executor.execute {
            targetDao.uprateStatus(id, status)
        }
    }

    fun delete(id: Long) {
        executor.execute {
            targetDao.delete(id)
        }
    }

    fun toHistory(id: Long, idShop: Long, price: Int, idCurrency: Long, idUser: Long) {
        executor.execute {
            targetDao.toHistory(id, idShop, price, idCurrency, idUser, Date().time)
        }
    }
}