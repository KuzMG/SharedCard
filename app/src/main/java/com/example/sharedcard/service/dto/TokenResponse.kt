package com.example.sharedcard.service.dto

import com.example.sharedcard.di.module.ServiceModule

data class TokenResponse(
    val token: String,
    val pic: String
){
    val url: String
        get() = ServiceModule.URL_REST+"/$pic"
}
