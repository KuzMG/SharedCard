package com.example.sharedcard.ui.check.target

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class TargetViewModel @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val targetRepository: TargetRepository
) : ViewModel() {

    private val userId: UUID
        get() = queryPreferences.userId
    private val groupId: UUID
        get() = queryPreferences.groupId
    val quickDelete: Boolean
        get() = queryPreferences.quickDelete
    val targetItemLiveData: LiveData<List<Target>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = ""
        targetItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetRepository.getAllCheck(groupId)
            } else {
                targetRepository.getAllQuery(groupId, query)
            }
        }
    }

    fun deleteTarget(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            targetRepository.delete(id)
        }
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }
}