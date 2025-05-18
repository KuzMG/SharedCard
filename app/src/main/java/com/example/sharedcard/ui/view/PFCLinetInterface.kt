package com.example.sharedcard.ui.view

interface PFCLinetInterface {

    fun setDataChart(protein: Float, fat: Float,carb: Float)
    fun clear()
    fun startAnimation()
    fun startWithoutAnimation()
    fun addOnClickListener(click:(Int,String) -> Unit)
}