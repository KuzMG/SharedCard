package com.example.sharedcard.ui.group.create_group

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.group.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor(private val groupManager: GroupManager) :
    ViewModel() {


    var name = ""
        set(value) {
            mutableStateLiveData.value = value.isNotEmpty()
            field = value
        }
    private val mutableStateLiveData = MutableLiveData<Boolean>()
    private val mutableResultLiveData = MutableLiveData<Result>()
    val stateLiveData: LiveData<Boolean>
        get() = mutableStateLiveData
    val resultLiveData: LiveData<Result>
        get() = mutableResultLiveData


    fun create(isInternetConnection: Boolean, pic: Bitmap) {
        mutableResultLiveData.postValue(Result(Result.State.LOADING))
        viewModelScope.launch(Dispatchers.Default) {
            groupManager.createGroup(isInternetConnection, name, pic).subscribe({
                mutableResultLiveData.postValue(Result(Result.State.OK))
            }, { error ->
                mutableResultLiveData.postValue(Result(Result.State.ERROR,error))
            })
        }
    }

}