package com.example.sharedcard.database.entity.target

import java.util.Date

data class Target(
    val id: Long,
    val name: String,
    val status: Int,
    val category: String,
    private val price: Int,
    private val currency: String,
    val creator: String? = null,
    private val dateFirst: Long
) {
    val priceList: String
        get() = "$price $currency"
    val date: Date
        get() = Date(dateFirst)
}
