package com.example.sharedcard.service.dto

import java.util.UUID

data class FileResponse(
    val id: UUID,
    val uri: String,
    val pic: ByteArray
)
