package com.example.sharedcard.ui.group.data

import androidx.annotation.StringRes

data class Result(val state:State,@StringRes val error: Int? = null){
    enum class State{
        LOADING,
        ERROR,
        OK
    }
}

