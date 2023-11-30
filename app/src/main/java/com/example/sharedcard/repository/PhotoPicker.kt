package com.example.sharedcard.repository

import android.content.Context
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import java.io.File


class PhotoPicker private constructor(context: Context) {

    private val filesDir = context.applicationContext.filesDir


    fun getUserAvatarFile(user: UserEntity): File {
        val imagePath = File(filesDir, "photos/user/")
        if (!imagePath.exists()) {
            imagePath.mkdirs()
        }
        return File(imagePath, user.photoFileName)
    }

    fun getGroupAvatarFile(group: GroupEntity): File {
        val imagePath = File(filesDir, "photos/group/")
        if (!imagePath.exists()) {
            imagePath.mkdir()
        }
        return File(imagePath, group.photoFileName)
    }

    companion object {
        private var INSTANCE: PhotoPicker? = null
        fun getInstance(context: Context): PhotoPicker {
            if (INSTANCE == null) {
                INSTANCE = PhotoPicker(context)
            }
            return INSTANCE!!
        }
    }
}