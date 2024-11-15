package com.example.sharedcard.ui.check.delete_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.CheckManager
import com.example.sharedcard.repository.TargetManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class DeleteItemViewModel(
    private val page: Int,
    private val checkManager: CheckManager,
    private val targetManager: TargetManager
) : ViewModel() {

    private val _sendLiveData = MutableLiveData<Throwable?>()
    val sendLiveData: LiveData<Throwable?>
        get() = _sendLiveData

    fun deleteItem(isInternetConnection: Boolean, id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            when (page) {
                0 -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        checkManager.delete(isInternetConnection, id).subscribe({
                            _sendLiveData.postValue(null)
                        }, { error ->
                            _sendLiveData.postValue(error)
                        })
                    }
                }

                1 -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        targetManager.delete(isInternetConnection,id).subscribe({
                            _sendLiveData.postValue(null)
                        }, { error ->
                            _sendLiveData.postValue(error)
                        })
                    }
                }
            }
        }
    }

    class ViewModelFactory @AssistedInject constructor(
        @Assisted private val page: Int,
        private val checkManager: CheckManager,
        private val targetManager: TargetManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DeleteItemViewModel::class.java)
            return DeleteItemViewModel(page, checkManager, targetManager) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted page: Int): ViewModelFactory
    }
}