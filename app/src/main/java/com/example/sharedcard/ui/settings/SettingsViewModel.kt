package com.example.sharedcard.ui.settings

import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.QueryPreferences
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val queryPreferences: QueryPreferences) :
    ViewModel() {

    var quickDelete: Boolean
        get() = queryPreferences.quickDelete
        set(value) {
            queryPreferences.quickDelete = value
        }
}