package com.example.sharedcard.database.entity.history

data class History(
    val name: String,
    val category: String,
    val shop: String,
    val buyer: String?,
    private val count: Int,
    private val metric: String,
    private val lastPrice: Int,
    private val lastCurrency: String,
    val dateLast: Long
){
    val quantity
        get() = "$count $metric"

    val priceList
        get() = "$lastPrice $lastCurrency"
}