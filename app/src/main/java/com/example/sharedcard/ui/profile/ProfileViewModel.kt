package com.example.sharedcard.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.AccountManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val accountManager: AccountManager) : ViewModel() {



    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setName(name)
        }
    }

    fun setHeight(height: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setHeight(height)
        }
    }

    fun setWeight(weight: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setWeight(weight)
        }
    }

    fun setAge(age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setAge(age)
        }
    }

    fun getUser() = accountManager.getUser()


}