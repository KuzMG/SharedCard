package com.example.sharedcard.repository

import android.content.Context
import androidx.core.content.edit

private const val PREF_MY_ACCOUNT = "account"
private const val PREF_NAME = "CONFIG"

class QueryPreferences private constructor(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var accountId: Long
        get() = prefs
            .getLong(PREF_MY_ACCOUNT, 0)
        set(value) {
            prefs.edit {
                putLong(PREF_MY_ACCOUNT, value)
            }
        }


    companion object {
        private var INSTANCE: QueryPreferences? = null
        fun getInstance(context: Context): QueryPreferences {
            if (INSTANCE == null) {
                INSTANCE = QueryPreferences(context)
            }
            return INSTANCE!!
        }
    }
}