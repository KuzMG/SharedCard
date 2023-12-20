package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.check.CheckEntity
import java.util.Date
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
        id: Long
    ) = checkDao.getAllForCheck(id)


    fun getAllQuery(
        id: Long,
        query: String
    ) = checkDao.getAllForCheckQuery(id, "$query%")

    fun getAllHistory(
        id: Long
    ) = checkDao.getAllForHistory(id)


    fun getAllHistoryQuery(
        id: Long,
        query: String
    ) = checkDao.getAllForHistoryQuery(id, "$query%")


    fun add(product: CheckEntity) {
        executor.execute {
            checkDao.add(product)
        }
    }

    fun setStatus(id: Long, status: Int) {
        executor.execute {
            checkDao.uprateStatus(id, status)
        }
    }

    fun delete(id: Long) {
        executor.execute {
            checkDao.delete(id)
        }
    }

    fun toHistory(id: Long, idShop: Long, price: Int, idCurrency: Long, idUser: Long) {
        executor.execute {
            checkDao.toHistory(id,idShop,price,idCurrency,idUser, Date().time)
        }
    }
}