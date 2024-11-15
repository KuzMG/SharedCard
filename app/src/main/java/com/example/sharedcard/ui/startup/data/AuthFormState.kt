package com.example.sharedcard.ui.startup.data

data class AuthFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
