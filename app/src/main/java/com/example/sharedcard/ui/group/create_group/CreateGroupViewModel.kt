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



    private val mutableResultLiveData = MutableLiveData<Result>()
    val resultLiveData: LiveData<Result>
        get() = mutableResultLiveData


    fun create(name: String, pic: Bitmap) {
        mutableResultLiveData.postValue(Result(Result.State.LOADING))
        if(name.isBlank()){
            mutableResultLiveData.value =Result(Result.State.ERROR,NoSuchFieldException("пусто"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.createGroup(name, pic).blockingGet()?.let{
                mutableResultLiveData.postValue(Result(Result.State.ERROR,it))
                return@launch
            }
            mutableResultLiveData.postValue(Result(Result.State.OK))
        }
    }

}