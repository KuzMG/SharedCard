package com.example.sharedcard.ui.bottom_navigation.check.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.repository.TargetRepository

class TargetViewModel(application: Application) : AndroidViewModel(application) {
    private val targetRepository: TargetRepository
    private val accountId: Long
    val targetItemLiveData: LiveData<List<Target>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        accountId = (application as SharedCardApp).getQueryPreferences().userId
        targetRepository = application.getTargetRepository()
        mutableSearch.value = ""
        targetItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetRepository.getAllCheck()
            } else {
                targetRepository.getAllQuery(query = query)
            }
        }
    }

    fun setCheckBox(id: Long, isChecked: Boolean) {
        val status = if (isChecked) 1 else 0
        targetRepository.setStatus(id, status)
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }
}