package com.example.sharedcard.ui.group.user_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class UserGroupViewModel @Inject constructor(private val groupManager: GroupManager) : ViewModel() {


    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
    private val _sendLiveData = MutableLiveData<Throwable?>()




    private fun getCurrentGroupId() = groupManager.getCurrentGroupId()


    fun deleteUser(isInternetConnection: Boolean, userId: UUID, groupId: UUID) {
        viewModelScope.launch(Dispatchers.Default) {
            groupManager.deleteUser(isInternetConnection, userId, groupId).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }


    fun setUserStatus(isInternetConnection: Boolean, idUser: UUID, idGroup: UUID,status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.setUserStatus(isInternetConnection, idUser, idGroup,status).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }


}