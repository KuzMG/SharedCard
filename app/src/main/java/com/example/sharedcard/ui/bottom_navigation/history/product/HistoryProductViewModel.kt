package com.example.sharedcard.ui.bottom_navigation.history.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.CheckHistory
import com.example.sharedcard.repository.CheckRepository

class HistoryProductViewModel(application: Application) : AndroidViewModel(application) {
    private val checkRepository: CheckRepository
    private val userId: Long
    private val groupId: Long
    val historyItemLiveData: LiveData<List<CheckHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        (application as SharedCardApp).getQueryPreferences().apply {
            this@HistoryProductViewModel.userId = userId
            this@HistoryProductViewModel.groupId = groupId
        }
        checkRepository = application.getProductRepository()
        mutableSearch.value = ""
        historyItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                checkRepository.getAllHistory(groupId)
            } else {
                checkRepository.getAllHistoryQuery(groupId,query)
            }
        }
    }


    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}