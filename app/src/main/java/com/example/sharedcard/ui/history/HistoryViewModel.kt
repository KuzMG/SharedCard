package com.example.sharedcard.ui.history

import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.PurchaseManager
import java.util.UUID
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val purchaseManager: PurchaseManager,
    private val groupManager: GroupManager,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    fun getHistory() =purchaseManager.getAllHistory()
    fun getPurchase(purchaseId: UUID) = purchaseManager.getById(purchaseId)
    fun getPerson(personId: UUID) = groupManager.getPersonById(personId)
    fun getShop(shopId: Int) = dictionaryRepository.getShopById(shopId)
    fun getGroup(groupId: UUID) = groupManager.getGroupById(groupId)



}