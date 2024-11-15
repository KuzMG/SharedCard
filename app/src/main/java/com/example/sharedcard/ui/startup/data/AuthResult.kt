package com.example.sharedcard.ui.startup.data

data class AuthResult(
    val loading: Boolean = false,
    val error: Exception? = null,
    val message: Int? = null
)
