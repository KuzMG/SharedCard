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

    val currentGroupLiveData: LiveData<UUID>
        get() = mutableCurrentGroupLiveData
    private val mutableCurrentGroupLiveData = MutableLiveData<UUID>()



    val sendDekGroupLiveData: LiveData<Pair<Throwable,UUID>>
        get() = _sendDekGroupLiveData
    private val _sendDekGroupLiveData = MutableLiveData<Pair<Throwable,UUID>>()
    init {
        mutableCurrentGroupLiveData.value = getCurrentGroupId()
    }

fun getCurrentGroupIdLiveData() = groupManager.groupChangedLiveData


    fun isLocalGroup() = groupManager.isLocalGroup()
    private fun getCurrentGroupId() = groupManager.getCurrentGroupId()


    fun setGroupToLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.setGroupToLocal()
            mutableCurrentGroupLiveData.postValue(getCurrentGroupId())
        }
    }

    fun setGroup(idGroup: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.setGroup(idGroup)
            mutableCurrentGroupLiveData.postValue(idGroup)
        }
    }




    private val _loadingLiveData = MutableLiveData<Result>()
    val loadingLiveData: LiveData<Result>
        get() = _loadingLiveData
    fun join(isInternetConnection: Boolean, token: String) {
        _loadingLiveData.postValue(Result(Result.State.LOADING))
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.joinInGroup(isInternetConnection, token).subscribe({
                _loadingLiveData.postValue(Result(Result.State.OK))
            }, { error ->
                _loadingLiveData.postValue(Result(Result.State.ERROR,error))
            })
        }
    }




    fun deleteGroup(isInternetConnection: Boolean, idGroup: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.deleteGroup(isInternetConnection, idGroup).subscribe({}, { error ->
                    _sendDekGroupLiveData.postValue(error to idGroup)
            })
        }
    }


    fun getStatus(idGroup: UUID) = groupManager.getStatus(idGroup)

    fun getGroups() = groupManager.getAllGroups()
    fun getMyId(): UUID = groupManager.getMyId()
}