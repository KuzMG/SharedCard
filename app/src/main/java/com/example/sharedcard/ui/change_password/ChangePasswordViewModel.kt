package com.example.sharedcard.ui.change_password

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedcard.email.EmailMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {
    var email = ""
    var code = ""
    var password = ""
    var repeatPassword = ""
    private val genCode: String
    init {
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
        if(email.isEmpty() || code.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            return 0
        } else if(code!= genCode){
            return 1
        } else if (password!=repeatPassword){
            return 2
        } else {
            return 3
        }
    }

    fun change(){

    }
}