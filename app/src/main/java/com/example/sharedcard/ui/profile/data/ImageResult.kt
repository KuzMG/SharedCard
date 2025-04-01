package com.example.sharedcard.ui.profile.data

import android.graphics.Bitmap
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.ui.group.data.Result

data class ImageResult(
    val state: Result.State,
    val userImage: UserImage? = null,
    val error: Throwable? = null
)

data class UserImage(
    val user: PersonEntity,
    val bitmap: Bitmap,
)
