package com.example.sharedcard.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.group.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class GroupViewModel @Inject constructor(private val groupManager: GroupManager) : ViewModel() {
    val sendDelGroupLiveData: LiveData<Pair<Throwable,UUID>>
        get() = _sendDekGroupLiveData
    private val _sendDekGroupLiveData = MutableLiveData<Pair<Throwable,UUID>>()

    private val _loadingLiveData = MutableLiveData<Result>()
    val loadingLiveData: LiveData<Result>
        get() = _loadingLiveData
    fun join(token: String) {
        _loadingLiveData.postValue(Result(Result.State.LOADING))
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.joinInGroup(token).subscribe({
                _loadingLiveData.postValue(Result(Result.State.OK))
            }, { error ->
                _loadingLiveData.postValue(Result(Result.State.ERROR,error))
            })
        }
    }

    fun deleteGroup(idGroup: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.deleteGroup(idGroup).blockingGet()?.let {
                _sendDekGroupLiveData.postValue(it to idGroup)
            }
        }
    }


    fun getStatus(idGroup: UUID) = groupManager.getStatus(idGroup)

    fun getGroups() = groupManager.getGroupsWithoutDefault()
    fun getMyId(): UUID = groupManager.getMyId()
    fun getPersons(groupId: UUID) = groupManager.getPersonsByGroup(groupId)
}