package com.example.sharedcard.ui.group.join_group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoinGroupViewModel @Inject constructor(private val groupManager: GroupManager) : ViewModel() {

    fun join(){
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.addUserInGroup()
        }
    }
}