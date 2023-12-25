package com.example.sharedcard

import android.app.Application
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupUsersRepository
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository


class SharedCardApp : Application() {
    fun getQueryPreferences() = QueryPreferences.getInstance(this)
    fun getDatabase() = AppDatabase.getInstance(this)
    fun getCheckRepository() = CheckRepository.getInstance(getDatabase())
    fun getDictionaryRepository() = DictionaryRepository.getInstance(getDatabase())
    fun getTargetRepository() = TargetRepository.getInstance(getDatabase())
    fun getGroupUsersRepository() = GroupUsersRepository.getInstance(getDatabase())
}