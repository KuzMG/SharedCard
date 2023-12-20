package com.example.sharedcard.ui.bottom_navigation.check.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository

class TargetViewModel(application: Application) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences
    private val targetRepository: TargetRepository
    private val userId: Long
        get() = queryPreferences.userId
    private val groupId: Long
        get() = queryPreferences.groupId
    val quickDelete: Boolean
        get() = queryPreferences.quickDelete
    val targetItemLiveData: LiveData<List<Target>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        targetRepository = application.getTargetRepository()
        mutableSearch.value = ""
        targetItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetRepository.getAllCheck(groupId)
            } else {
                targetRepository.getAllQuery(groupId,query)
            }
        }
    }

    fun deleteTarget(id: Long) {
        targetRepository.delete(id)
    }
    fun setQuery(query: String) {
        mutableSearch.value = query
    }
}