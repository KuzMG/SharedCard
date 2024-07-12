package com.example.sharedcard.ui.check.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.QueryPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    private val checkRepository: CheckRepository,
    private val queryPreferences: QueryPreferences,
    private val groupManager: GroupManager
) : ViewModel() {

    val groupChanged: LiveData<UUID>
        get() = groupManager.groupChangedLiveData

    val quickDelete: Boolean
        get() = queryPreferences.quickDelete
    val checkItemLiveData: LiveData<List<Check>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = ""
        checkItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                checkRepository.getAllCheck()
            } else {
                checkRepository.getAllQuery(query)
            }
        }
    }



    fun setCheckBox(id: UUID, isChecked: Boolean) {
        val status = if (isChecked) 1 else 0
        viewModelScope.launch(Dispatchers.IO) {
            checkRepository.setStatus(id, status)
        }
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }

    fun deleteCheck(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            checkRepository.delete(id)
        }
    }
}