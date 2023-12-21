package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.check.CheckEntity
import java.util.Date
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CheckRepository private constructor(database: AppDatabase) {
    companion object {
        private var nCheckRepository: CheckRepository? = null
        fun getInstance(database: AppDatabase): CheckRepository {
            if (nCheckRepository == null) {
                nCheckRepository = CheckRepository(database)
            }
            return nCheckRepository!!
        }
    }

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val checkDao = database.checkDao()

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
        executor.execute {
            checkDao.add(product)
        }
    }

    fun setStatus(id: UUID, status: Int) {
        executor.execute {
            checkDao.uprateStatus(id, status)
        }
    }

    fun delete(id: UUID) {
        executor.execute {
            checkDao.delete(id)
        }
    }

    fun toHistory(id: UUID, idShop: Long, price: Int, idCurrency: Long, idUser: UUID) {
        executor.execute {
            checkDao.toHistory(id,idShop,price,idCurrency,idUser, Date().time)
        }
    }
}