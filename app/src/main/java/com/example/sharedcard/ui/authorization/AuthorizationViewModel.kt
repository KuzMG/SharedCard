package com.example.sharedcard.ui.authorization

import androidx.lifecycle.ViewModel

class AuthorizationViewModel : ViewModel() {
    var login = ""
    var password = ""


    fun check(): Int{
        if(login.isEmpty() || password.isEmpty()) {
            return 0
        } else{
            return 1
        }
    }

    fun signIn(){

    }
}