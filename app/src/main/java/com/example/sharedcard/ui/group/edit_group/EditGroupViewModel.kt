package com.example.sharedcard.ui.group.edit_group

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.ui.group.data.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class EditGroupViewModel(
    private val idGroup: UUID,
    private val groupManager: GroupManager
) :
    ViewModel() {
    var isImageChange = false
    private val mutableStateLiveData = MutableLiveData<Boolean>()
    private val mutableResultLiveData = MutableLiveData<Result>()
    val stateLiveData: LiveData<Boolean>
        get() = mutableStateLiveData
    val resultLiveData: LiveData<Result>
        get() = mutableResultLiveData


    fun getGroup() = groupManager.getGroup(idGroup)
    fun savePic(isInternetConnection: Boolean, bitmap: Bitmap) {
        mutableResultLiveData.postValue(Result(Result.State.LOADING))
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.editGroupPic(isInternetConnection, idGroup, bitmap)
                .subscribe({
                    mutableResultLiveData.postValue(Result(Result.State.OK))
                }, { error ->
                    mutableResultLiveData.postValue(Result(Result.State.ERROR,error))
                })
        }
    }

    fun saveName(isInternetConnection: Boolean, name: String) {
        mutableResultLiveData.postValue(Result(Result.State.LOADING))
        viewModelScope.launch(Dispatchers.IO) {
            groupManager.editGroup(isInternetConnection, idGroup, name)
                .subscribe({
                    mutableResultLiveData.postValue(Result(Result.State.OK))
                }, { error ->
                    mutableResultLiveData.postValue(Result(Result.State.ERROR,error))
                })
        }
    }

    fun setState(value: String) {
        mutableStateLiveData.value = value.isNotEmpty()
    }

    class ViewModelFactory @AssistedInject constructor(
        @Assisted("idGroup") private val idGroup: String,
        private val groupManager: GroupManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EditGroupViewModel::class.java)
            return EditGroupViewModel(UUID.fromString(idGroup), groupManager) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted("idGroup") idGroup: String): ViewModelFactory
    }
}