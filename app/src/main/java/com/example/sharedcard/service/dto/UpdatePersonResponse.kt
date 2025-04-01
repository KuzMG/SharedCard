package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.person.PersonEntity

data class UpdatePersonResponse(
    val person: PersonEntity
)