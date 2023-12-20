package com.example.sharedcard.repository

import android.content.Context
import androidx.core.content.edit

private const val PREF_USER = "user"
private const val PREF_GROUP = "group"
private const val PREF_LOCAL = "local"
private const val PREF_NAME = "CONFIG"
private const val PREF_QUICK_DELETE = "quick_delete"

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

    var isLocal: Boolean
        get() = prefs
            .getBoolean(PREF_LOCAL, true)
        set(value) {
            prefs.edit {
                putBoolean(PREF_LOCAL, value)
            }
        }

    var quickDelete: Boolean
        get() = prefs
            .getBoolean(PREF_QUICK_DELETE, false)
        set(value) {
            prefs.edit {
                putBoolean(PREF_QUICK_DELETE, value)
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