package com.example.sharedcard.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.user.UserEntity
import kotlinx.coroutines.Deferred
import java.io.File
import java.io.FileOutputStream


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

    fun savePhoto(bitmap: Bitmap,photoFile: File){
        val stream = FileOutputStream(photoFile)
        stream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
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