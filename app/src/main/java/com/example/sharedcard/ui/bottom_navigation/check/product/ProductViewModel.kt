package com.example.sharedcard.ui.bottom_navigation.check.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import java.util.UUID

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val checkRepository: CheckRepository
    private val queryPreferences: QueryPreferences
    private val groupId: UUID
        get() = queryPreferences.groupId
    val quickDelete: Boolean
        get() = queryPreferences.quickDelete
    val checkItemLiveData: LiveData<List<Check>>
    private val mutableSearch = MutableLiveData<String>()
    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()
        checkRepository = application.getProductRepository()
        mutableSearch.value = ""
        checkItemLiveData = mutableSearch.switchMap { query ->
            if (query.isBlank()) {
                checkRepository.getAllCheck(groupId)
            } else {
                checkRepository.getAllQuery(groupId,query)
            }
        }
    }

    fun setCheckBox(id: UUID, isChecked: Boolean) {
        val status = if (isChecked) 1 else 0
        checkRepository.setStatus(id, status)
    }

    fun setQuery(query: String) {
        mutableSearch.value = query
    }
    fun deleteCheck(id: UUID) {
        checkRepository.delete(id)
    }
}