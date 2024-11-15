package com.example.sharedcard.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.ui.profile.data.ImageResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val accountManager: AccountManager) :
    ViewModel() {

    private val mutableLoadingLiveData = MutableLiveData<Result>()
    val loadingLiveData: LiveData<Result>
        get() = mutableLoadingLiveData

    fun setName(name: String) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setName(name).subscribe({
                mutableLoadingLiveData.postValue(Result(Result.State.OK))
            }, { e ->
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR,e))
            })
        }
    }

    fun setHeight(height: Int) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setHeight(height).subscribe({
                mutableLoadingLiveData.postValue(Result(Result.State.OK))
            }, { e ->
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR,e))
            })
        }
    }

    fun setWeight(weight: Double) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setWeight(weight).subscribe({
                mutableLoadingLiveData.postValue(Result(Result.State.OK))
            }, { e ->
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR,e))
            })
        }
    }

    fun setDate(date: Long) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setDate(date).subscribe({
                mutableLoadingLiveData.postValue(Result(Result.State.OK))
            }, { e ->
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR,e))
            })
        }
    }

    fun getUser() = accountManager.getUser()
    fun getUserAccount() = accountManager.getUserAccountLiveData()
    fun setImage(isInternetConnection: Boolean, bitmap: Bitmap) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.Default) {
            val result = accountManager.setImage(isInternetConnection, bitmap)
            mutableLoadingLiveData.postValue(result)
        }
    }

}