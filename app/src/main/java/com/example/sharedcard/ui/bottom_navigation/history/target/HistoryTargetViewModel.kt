package com.example.sharedcard.ui.bottom_navigation.history.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.CheckHistory
import com.example.sharedcard.database.entity.target.TargetHistory
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import java.util.UUID

class HistoryTargetViewModel(application: Application) : AndroidViewModel(application) {
    private val targetRepository: TargetRepository
    private val queryPreferences: QueryPreferences
    private val groupId: UUID
        get() = queryPreferences.groupId
    val historyItemLiveData: LiveData<List<TargetHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        targetRepository = application.getTargetRepository()
        mutableSearch.value = ""
        historyItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetRepository.getAllHistory(groupId)
            } else {
                targetRepository.getAllHistoryQuery(groupId,query)
            }
        }
    }


    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}