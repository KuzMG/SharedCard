package com.example.sharedcard.repository

import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.database.entity.product.ProductEntity
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val productDao = database.productDao()

    fun getAllCheck(
        id: Long = 0
    ) = productDao.getAllForCheck(id)


    fun getAllQuery(
        id: Long = 0,
        query: String
    ) = productDao.getAllForCheckQuery(id, "$query%")


    fun add(product: ProductEntity) {
        executor.execute {
            productDao.add(product)
        }
    }

    fun setStatus(id: Long, status: Int) {
        executor.execute {
            productDao.uprateStatus(id, status)
        }
    }

    fun delete(id: Long) {
        executor.execute {
            productDao.delete(id)
        }
    }

    fun toHistory(id: Long, idShop: Long, price: Int, idCurrency: Long, idUser: Long? = null) {
        executor.execute {
            productDao.toHistory(id,idShop,price,idCurrency,idUser, Date().time)
        }
    }
}