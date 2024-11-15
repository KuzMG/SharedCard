package com.example.sharedcard.ui.startup.data

data class RegisterResult(
    val codeSend: Boolean = false,
    val isContinue: Boolean = false,
    val loading: Boolean = false,
    val error: Exception? = null,
    val message: Int? = null
)
