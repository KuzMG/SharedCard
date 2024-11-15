package com.example.sharedcard.ui.check.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.repository.CheckManager
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.QueryPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    private val checkManager: CheckManager,
    private val queryPreferences: QueryPreferences,
    private val groupManager: GroupManager
) : ViewModel() {
    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
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
                checkManager.getAllCheck()
            } else {
                checkManager.getAllQuery(query)
            }
        }
    }



    fun setCheckBox(id: UUID, isChecked: Boolean) {
        val status = if (isChecked) CheckEntity.CHECKED else CheckEntity.UNCHECKED
        viewModelScope.launch(Dispatchers.IO) {
            checkManager.setStatus(id, status)
        }
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }

    fun deleteCheck(isInternetConnection: Boolean,id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            checkManager.delete(isInternetConnection, id).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }
}