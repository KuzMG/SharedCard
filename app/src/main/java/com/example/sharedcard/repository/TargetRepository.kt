package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.project.shared_card.database.dao.target.TargetEntity
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
        id: Long = 0
    ) = targetDao.getAllForCheck(id)


    fun getAllQuery(
        id: Long = 0,
        query: String
    ) = targetDao.getAllForCheckQuery(id, "$query%")


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
}