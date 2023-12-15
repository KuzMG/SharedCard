package com.example.sharedcard.ui.registration

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.email.EmailMessage
import com.example.sharedcard.repository.PhotoPicker
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.util.getScaledBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random

private const val TAG = "RegistrationViewModel"

class RegistrationViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences




    var name = ""
    var email = ""
    var code = ""
    private val genCode: String
    var password = ""
    var repeatPassword = ""


    init {
        queryPreferences = (application as SharedCardApp).getQueryPreferences()

        val strBuilder = StringBuilder()
        for (i in 1..6)
            strBuilder.append(Random.nextInt(0,10))
        genCode = strBuilder.toString()
    }

    fun sendMessage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                EmailMessage.send(email, genCode)
            }
        }
    }

    fun check() : Int{
        if(name.isEmpty() || email.isEmpty() || code.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            return 0
        } else if(code!= genCode){
            return 1
        } else if (password!=repeatPassword){
            return 2
        } else {
            return 3
        }
    }

    fun create(){

    }

//    private fun getUserId() {
//        userApi.addUser(user).enqueue(object : Callback<Long> {
//            override fun onResponse(call: Call<Long>, response: Response<Long>) {
//                val id = response.body() ?: throw Exception()
//                queryPreferences.userId = id
//                user.id = id
//                photoFile = photoPicker.getUserAvatarFile(user)
//                Log.i(TAG, "user is created")
//            }
//
//            override fun onFailure(call: Call<Long>, t: Throwable) {
//                Log.e(TAG, "user is not created", t)
//            }
//        })
//    }

//    fun saveAccount() {
//        if (photoFile.exists() && user.name.isNotEmpty()) {
//            executor.execute {
//                userApi.updateName(user.id, user.name)
//                userApi.updatePhoto(user.id, photoFile.readBytes())
//            }
//        }
//    }
    }