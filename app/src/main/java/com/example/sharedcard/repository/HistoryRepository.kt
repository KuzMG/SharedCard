package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase

class HistoryRepository private constructor(database: AppDatabase) {
    private val historyDao = database.historyDao()

//    fun getAll(id: Long = 0) = historyDao.getAll(id)
//
//    fun getAllQuery(id: Long = 0, query: String) = historyDao.getAllForQuery(id, "$query%")

    companion object {
        private var nRepository: HistoryRepository? = null
        fun getInstance(database: AppDatabase): HistoryRepository {
            if (nRepository == null) nRepository = HistoryRepository(database)
            return nRepository!!
        }
    }
}