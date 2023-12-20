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
import com.example.sharedcard.repository.TargetRepository

class HistoryTargetViewModel(application: Application) : AndroidViewModel(application) {
    private val targetRepository: TargetRepository
    private val userId: Long
    private val groupId: Long
    val historyItemLiveData: LiveData<List<TargetHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        (application as SharedCardApp).getQueryPreferences().apply {
            this@HistoryTargetViewModel.userId = userId
            this@HistoryTargetViewModel.groupId = groupId
        }
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