package com.example.sharedcard.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.sharedcard.database.AppDatabase

class DictionaryRepository private constructor(database: AppDatabase) {
    private val shopProductDao = database.shopProductDao()
    private val categoryProductDao = database.categoryProductDao()
    private val metricDao = database.metricDao()
    private val shopTargetDao = database.shopTargetDao()
    private val categoryTargetDao = database.categoryTargetDao()
    private val currencyDao= database.currencyDao()

    fun getAllShopsProduct(): LiveData<List<String>> = shopProductDao.getAll().map { list ->
        list.map { shop ->
            shop.name
        }
    }

    fun getAllCategoriesProduct(): LiveData<List<String>> = categoryProductDao.getAll().map { list ->
        list.map { category ->
            category.name
        }
    }

    fun getAllMetrics(): LiveData<List<String>> = metricDao.getAll().map { list ->
        list.map { metric ->
            metric.name
        }
    }

    fun getAllShopsTarget(): LiveData<List<String>> = shopTargetDao.getAll().map { list ->
        list.map { shop ->
            shop.name
        }
    }

    fun getAllCategoriesTarget(): LiveData<List<String>> = categoryTargetDao.getAll().map { list ->
        list.map { category ->
            category.name
        }
    }

    fun getAllCurrency(): LiveData<List<String>> = currencyDao.getAll().map { list ->
        list.map { currency ->
            currency.name
        }
    }

    companion object {
        private var nRepository: DictionaryRepository? = null
        fun getInstance(database: AppDatabase): DictionaryRepository {
            if (nRepository == null) nRepository = DictionaryRepository(database)
            return nRepository!!
        }
    }
}