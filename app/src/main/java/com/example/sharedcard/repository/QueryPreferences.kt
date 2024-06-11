package com.example.sharedcard.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_USER = "user"
private const val PREF_GROUP = "group"
private const val PREF_LOCAL = "local"
private const val PREF_NAME = "CONFIG"
private const val PREF_QUICK_DELETE = "quick_delete"
@Singleton
class QueryPreferences @Inject  constructor(app: Application) {

    companion object {
        const val DEF_VALUE = "00000000-0000-0000-0000-000000000000"
    }

    private val prefs = app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var userId: UUID
        get() = UUID.fromString(prefs
            .getString(PREF_USER, DEF_VALUE))
        set(value) {
            prefs.edit {
                putString(PREF_USER, value.toString())
            }
        }

    var groupId: UUID
        get() = UUID.fromString(prefs
            .getString(PREF_GROUP, DEF_VALUE))
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
}