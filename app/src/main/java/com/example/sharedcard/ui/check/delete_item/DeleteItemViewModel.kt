package com.example.sharedcard.ui.check.delete_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.TargetRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class DeleteItemViewModel(
    private val page: Int,
    private val checkRepository: CheckRepository,
    private val targetRepository: TargetRepository
) : ViewModel() {


    fun deleteItem(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            when (page) {
                0 -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        checkRepository.delete(id)
                    }
                }

                1 -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        targetRepository.delete(id)
                    }
                }
            }
        }
    }

    class ViewModelFactory @AssistedInject constructor(
        @Assisted private val page: Int,
        private val checkRepository: CheckRepository,
        private val targetRepository: TargetRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DeleteItemViewModel::class.java)
            return DeleteItemViewModel(page, checkRepository, targetRepository) as T
        }
    }
    @AssistedFactory
    interface FactoryHelper {
        fun create(@Assisted page: Int): ViewModelFactory
    }
}