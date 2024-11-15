package com.example.sharedcard.ui.history.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sharedcard.database.entity.check.CheckHistory
import com.example.sharedcard.repository.CheckManager
import javax.inject.Inject

class HistoryProductViewModel @Inject constructor(
    private val checkManager: CheckManager
) : ViewModel() {

    val historyItemLiveData: LiveData<List<CheckHistory>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = ""
        historyItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                checkManager.getAllHistory()
            } else {
                checkManager.getAllHistoryQuery(query)
            }
        }
    }


    fun setQuery(query: String) {
        mutableSearch.value = query
    }

}