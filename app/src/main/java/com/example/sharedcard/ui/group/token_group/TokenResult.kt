package com.example.sharedcard.ui.group.token_group

import com.example.sharedcard.service.dto.TokenResponse

data class TokenResult(
    val token: TokenResponse? = null, val error: String? = null
)
