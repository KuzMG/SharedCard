package com.example.sharedcard.repository

import android.content.Context
import androidx.room.Room
import com.example.sharedcard.database.AppDatabase
import retrofit2.http.Query

class ProductRepository private constructor(database: AppDatabase) {
    companion object {
        private var nProductRepository: ProductRepository? = null
        fun getInstance(database: AppDatabase): ProductRepository {
            if (nProductRepository == null) {
                nProductRepository = ProductRepository(database)
            }
            return nProductRepository!!
        }
    }

    private val productDao = database.productDao()

    fun getAllCheck(id: Long) = productDao.getAllForCheck(id)

    fun getAllQuery(id: Long, query: String) = productDao.getAllForCheckQuery(id, query)
}