package com.example.sharedcard.repository

import android.content.Context
import androidx.core.content.edit

private const val PREF_USER = "user"
private const val PREF_GROUP = "group"
private const val PREF_NAME = "CONFIG"

class QueryPreferences private constructor(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var userId: Long
        get() = prefs
            .getLong(PREF_USER, 0)
        set(value) {
            prefs.edit {
                putLong(PREF_USER, value)
            }
        }

    var groupId: Long
        get() = prefs
            .getLong(PREF_GROUP, 0)
        set(value) {
            prefs.edit {
                putLong(PREF_GROUP, value)
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