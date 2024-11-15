package com.example.sharedcard.service.dto

data class RegistrationBody(
    val email: String,
    val password: String,
    val name: String,
    val date: Long,
    val gender: Boolean,
    val weight: Double,
    val height: Int
)