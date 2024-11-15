package com.example.sharedcard.ui.history.target

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.entity.target.TargetHistory
import com.example.sharedcard.repository.TargetManager
import javax.inject.Inject

class HistoryTargetViewModel @Inject constructor(
    private val targetManager: TargetManager
) : ViewModel() {


    val historyItemLiveData: LiveData<List<TargetHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = ""
        historyItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetManager.getAllHistory()
            } else {
                targetManager.getAllHistoryQuery(query)
            }
        }
    }


    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}