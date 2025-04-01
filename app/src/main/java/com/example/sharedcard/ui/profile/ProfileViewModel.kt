package com.example.sharedcard.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.ui.group.data.Result
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
            accountManager.setName(name).blockingGet()?.let {
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR, it))
                return@launch
            }
            mutableLoadingLiveData.postValue(Result(Result.State.OK))
        }
    }

    fun setHeight(height: Int) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setHeight(height).blockingGet()?.let {
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR, it))
                return@launch
            }
            mutableLoadingLiveData.postValue(Result(Result.State.OK))
        }
    }

    fun setWeight(weight: Double) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setWeight(weight).blockingGet()?.let {
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR, it))
                return@launch
            }
            mutableLoadingLiveData.postValue(Result(Result.State.OK))
        }
    }

    fun setDate(date: Long) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            accountManager.setDate(date).blockingGet()?.let {
                mutableLoadingLiveData.postValue(Result(Result.State.ERROR, it))
                return@launch
            }
            mutableLoadingLiveData.postValue(Result(Result.State.OK))
        }
    }

    fun getUser() = accountManager.getPerson()
    fun getUserAccount() = accountManager.getAccountLiveData()
    fun setImage(bitmap: Bitmap) {
        mutableLoadingLiveData.value = Result(Result.State.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val result = accountManager.setImage(bitmap)
            mutableLoadingLiveData.postValue(result)
        }
    }

}