package com.example.sharedcard.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class GroupViewModel @Inject constructor(private val groupManager: GroupManager) : ViewModel() {

    val currentGroupLiveData: LiveData<UUID>
        get() = mutableCurrentGroupLiveData
    private val mutableCurrentGroupLiveData = MutableLiveData<UUID>()


    init {
        mutableCurrentGroupLiveData.value = getCurrentGroupId()
    }

    fun getToken(idGroup: UUID): LiveData<String> {
        return MutableLiveData("pt43f34k")
    }

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

    fun deleteUser(idUser: UUID, idGroup: UUID) {
        viewModelScope.launch(Dispatchers.Default) {
            groupManager.deleteUser(idUser, idGroup)
        }
    }

    fun deleteGroup(idGroup: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.deleteGroup(idGroup)
        }
    }

    fun makeUserAdmin(idUser: UUID, idGroup: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.makeUserAdmin(idUser, idGroup)
        }
    }

    fun isAdmin(idGroup: UUID) = groupManager.isAdmin(idGroup)

    fun getGroups() = groupManager.getAllGroups()
}