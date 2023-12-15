package com.example.sharedcard.ui.bottom_navigation.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.repository.HistoryRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRepository: HistoryRepository

//    val historyLiveData: LiveData<List<History>>
    private val searchQuery = MutableLiveData<String>()

    init {
        historyRepository = (application as SharedCardApp).getHistoryRepository()

        searchQuery.value = ""
//        historyLiveData = searchQuery.switchMap { query ->
//            if (query.isBlank()) {
//                historyRepository.getAll()
//            } else {
//                historyRepository.getAllQuery(query = query)
//            }
//        }
    }

    fun setQuery(query: String) {
        searchQuery.value = query
    }

}