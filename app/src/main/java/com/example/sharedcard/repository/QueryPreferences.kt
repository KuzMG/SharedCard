package com.example.sharedcard.repository

import android.content.Context
import androidx.core.content.edit
import java.util.UUID

private const val PREF_USER = "user"
private const val PREF_GROUP = "group"
private const val PREF_LOCAL = "local"
private const val PREF_NAME = "CONFIG"
private const val PREF_QUICK_DELETE = "quick_delete"

class QueryPreferences private constructor(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var userId: UUID
        get() = UUID.fromString(prefs
            .getString(PREF_USER, "00000000-0000-0000-0000-000000000000"))
        set(value) {
            prefs.edit {
                putString(PREF_USER, value.toString())
            }
        }

    var groupId: UUID
        get() = UUID.fromString(prefs
            .getString(PREF_GROUP, "00000000-0000-0000-0000-000000000000"))
        set(value) {
            prefs.edit {
                putString(PREF_GROUP, value.toString())
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