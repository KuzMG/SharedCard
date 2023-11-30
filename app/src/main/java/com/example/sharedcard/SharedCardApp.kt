package com.example.sharedcard

import android.app.Application
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.repository.DictionaryProductRepository
import com.example.sharedcard.repository.PhotoPicker
import com.example.sharedcard.repository.ProductRepository
import com.example.sharedcard.repository.QueryPreferences


class SharedCardApp : Application() {
    fun getPhotoPicker() = PhotoPicker.getInstance(this)
    fun getQueryPreferences() = QueryPreferences.getInstance(this)
    fun getDatabase() = AppDatabase.getInstance(this)
    fun getProductRepository() = ProductRepository.getInstance(getDatabase())
    fun getDictionaryProductRepository() = DictionaryProductRepository.getInstance(getDatabase())
}