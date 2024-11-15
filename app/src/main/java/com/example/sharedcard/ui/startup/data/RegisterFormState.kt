package com.example.sharedcard.ui.startup.data

data class RegisterFormState(
    val page: Int,
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val genderError: Int? = null,
    val passwordError: Int? = null,
    val codeError: Int? = null,
    val isCodeValid: Boolean = false,
    val isDataValid: Boolean = false
)
