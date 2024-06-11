package com.example.sharedcard.ui.startup.data

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val repeatPasswordError: Int? = null,
    val isDataValid: Boolean = false
)
