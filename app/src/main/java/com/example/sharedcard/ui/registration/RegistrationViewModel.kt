package com.example.sharedcard.ui.registration

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.repository.PhotoPicker
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.retrofit.SharedCardService
import com.example.sharedcard.retrofit.api.UserApi
import com.example.sharedcard.util.getScaledBitmap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

private const val TAG = "RegistrationViewModel"

class RegistrationViewModel(
    private val queryPreferences: QueryPreferences,
    private val photoPicker: PhotoPicker
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = checkNotNull(extras[APPLICATION_KEY]) as SharedCardApp
                return RegistrationViewModel(
                    app.getQueryPreferences(),
                    app.getPhotoPicker()
                ) as T
            }
        }
    }


    private val userApi = SharedCardService.getInstance().create(UserApi::class.java)
    private val savePhotoMutableLiveData = MutableLiveData<Bitmap>()
    private val executor = Executors.newSingleThreadExecutor()

    lateinit var photoFile: File

    val user = UserEntity()
    var widthPhotoView = 0
    var heightPhotoView = 0

    val savePhotoLiveData: LiveData<Bitmap>
        get() = savePhotoMutableLiveData

    init {
        val accountId = queryPreferences.accountId
        if (accountId == 0L) {
            getUserId()
        } else {
            user.id = accountId
        }
    }

    fun savePhoto(bitmap: Bitmap) {
        executor.execute {
            val stream = FileOutputStream(photoFile)
            stream.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            val bitmap = getScaledBitmap(photoFile.path, widthPhotoView, heightPhotoView)
            savePhotoMutableLiveData.postValue(bitmap)
        }
    }

    private fun getUserId() {
        userApi.addUser(user).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                val id = response.body() ?: throw Exception()
                queryPreferences.accountId = id
                user.id = id
                photoFile = photoPicker.getUserAvatarFile(user)
                Log.i(TAG, "user is created")
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                Log.e(TAG, "user is not created", t)
            }
        })
    }

    fun saveAccount() {
        if (photoFile.exists() && user.name.isNotEmpty()) {
            executor.execute {
                userApi.updateName(user.id, user.name)
                userApi.updatePhoto(user.id, photoFile.readBytes())
            }
        }
    }
}