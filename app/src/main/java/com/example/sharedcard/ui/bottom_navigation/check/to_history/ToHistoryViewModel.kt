package com.example.sharedcard.ui.bottom_navigation.check.to_history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sharedcard.SharedCardApp
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.CheckRepository
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.repository.TargetRepository
import java.util.UUID

class ToHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val queryPreferences: QueryPreferences
    private val dictionaryRepository: DictionaryRepository
    private val checkRepository: CheckRepository
    private val targetRepository: TargetRepository

    var page = 0
    var name = ""
    var shop = 0L
    var price = 0
    var id = UUID.fromString("00000000-0000-0000-0000-000000000000")
    private val idUser: UUID
        get() = queryPreferences.userId

    init {
        (application as SharedCardApp).apply {
            dictionaryRepository = getDictionaryRepository()
            checkRepository = getCheckRepository()
            targetRepository = getTargetRepository()
            queryPreferences = getQueryPreferences()
        }
    }

    var currency = 0L

    fun getShop() =
        when (page) {
            0 -> dictionaryRepository.getAllShopsProduct()
            1 -> dictionaryRepository.getAllShopsTarget()
            else -> throw IndexOutOfBoundsException()
        }

    fun getCurrency() = dictionaryRepository.getAllCurrency()

    fun toHistory() {
        when (page) {
            0 -> checkRepository.toHistory(id, shop, price, currency, idUser)
            1 -> targetRepository.toHistory(id, shop, price, currency, idUser)
        }

    }


}