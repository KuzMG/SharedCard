package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase

class DictionaryTargetRepository private constructor(database: AppDatabase) {
    val shopTargetDao = database.shopTargetDao()
    val categoryTargetDao = database.categoryTargetDao()
    val currencyDao= database.currencyDao()





    companion object {
        private var nRepository: DictionaryTargetRepository? = null
        fun getInstance(database: AppDatabase): DictionaryTargetRepository {
            if (nRepository == null)
                nRepository = DictionaryTargetRepository(database)
            return nRepository!!
        }
    }
}