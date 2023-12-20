package com.example.sharedcard.binding

import android.view.View
import android.widget.CheckBox
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun changeVisible(view: View, flag: Boolean) {
    if (flag) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("selectable")
fun changeCheckBox(view: View, status: Int) {
    (view as CheckBox).isChecked =  when(status){
        0 -> false
        1 -> true
        else -> throw IndexOutOfBoundsException()
    }
}
