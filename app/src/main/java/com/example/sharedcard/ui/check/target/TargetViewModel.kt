package com.example.sharedcard.ui.check.target

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class TargetViewModel @Inject constructor(
    private val queryPreferences: QueryPreferences,
    private val targetManager: TargetManager,
    private val groupManager: GroupManager
) : ViewModel() {
    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
    val groupChanged: LiveData<UUID>
        get() = groupManager.groupChangedLiveData

    val quickDelete: Boolean
        get() = queryPreferences.quickDelete
    val targetItemLiveData: LiveData<List<Target>>
    private val mutableSearch = MutableLiveData<String>()

    init {
        mutableSearch.value = ""
        targetItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                targetManager.getAllCheck()
            } else {
                targetManager.getAllQuery(query)
            }
        }
    }

    fun deleteTarget(isInternetConnection: Boolean,id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            targetManager.delete(isInternetConnection,id).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }
}