package com.example.sharedcard.ui.bottom_navigation.history.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.CheckHistory
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import java.util.UUID

class HistoryProductViewModel(application: Application) : AndroidViewModel(application) {
    private val checkRepository: CheckRepository
    private val queryPreferences: QueryPreferences

    private val groupId: UUID
        get() = queryPreferences.groupId
    val historyItemLiveData: LiveData<List<CheckHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        checkRepository = application.getCheckRepository()
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