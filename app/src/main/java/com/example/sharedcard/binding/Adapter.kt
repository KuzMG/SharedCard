package com.example.sharedcard.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun changeVisible(view: View, flag: Boolean) {
    if (flag) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}
