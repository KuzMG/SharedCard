package com.example.sharedcard.ui.group.token_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.service.dto.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class DialogTokenViewModel @Inject constructor(private val groupManager: GroupManager) :
    ViewModel() {
    val sendLiveData: LiveData<TokenResult>
        get() = _sendLiveData
    private val _sendLiveData = MutableLiveData<TokenResult>()

    fun getToken(groupId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            _sendLiveData.postValue(groupManager.getToken(groupId))
        }
    }
}