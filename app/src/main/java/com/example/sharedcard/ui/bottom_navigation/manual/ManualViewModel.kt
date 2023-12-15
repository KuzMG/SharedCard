package com.example.sharedcard.ui.bottom_navigation.manual

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.DictionaryRepository

class ManualViewModel(application: Application) : AndroidViewModel(application) {
    private val dictionaryRepository: DictionaryRepository
    init {
        dictionaryRepository = (application as SharedCardApp).getDictionaryRepository()
    }

    fun getCategories() = dictionaryRepository.getAllCategories()

}