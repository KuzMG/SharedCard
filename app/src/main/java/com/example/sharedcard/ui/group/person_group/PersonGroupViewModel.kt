package com.example.sharedcard.ui.group.person_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class PersonGroupViewModel @Inject constructor(private val groupManager: GroupManager) : ViewModel() {


    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData
    private val _sendLiveData = MutableLiveData<Throwable?>()




    private fun getCurrentGroupId() = groupManager.getCurrentGroupId()


    fun deletePerson(personId: UUID, groupId: UUID) {
        viewModelScope.launch(Dispatchers.Default) {
            groupManager.deletePerson(personId, groupId).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }


    fun setPersonStatus(personId: UUID, groupId: UUID, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.setPersonStatus(personId, groupId,status).subscribe({
                _sendLiveData.postValue(null)
            }, { error ->
                _sendLiveData.postValue(error)
            })
        }
    }


}