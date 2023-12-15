package com.example.sharedcard.ui.bottom_navigation.check.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.repository.CheckRepository

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val checkRepository: CheckRepository
    private val accountId: Long
    val checkItemLiveData: LiveData<List<Check>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        accountId = (application as SharedCardApp).getQueryPreferences().userId
        checkRepository = application.getProductRepository()
        mutableSearch.value = ""
        checkItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                checkRepository.getAllCheck()
            } else {
                checkRepository.getAllQuery(query = query)
            }
        }
    }

    fun setCheckBox(id: Long, isChecked: Boolean) {
        val status = if (isChecked) 1 else 0
        checkRepository.setStatus(id, status)
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}