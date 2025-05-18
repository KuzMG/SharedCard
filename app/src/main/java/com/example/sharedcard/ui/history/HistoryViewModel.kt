package com.example.sharedcard.ui.history

import androidx.lifecycle.ViewModel
import com.example.sharedcard.repository.DictionaryRepository
import com.example.sharedcard.repository.GroupManager
import com.example.sharedcard.repository.PurchaseManager
import com.example.sharedcard.ui.history.adapter.ViewModelHistoryAdapter
import java.util.UUID
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val purchaseManager: PurchaseManager,
    private val groupManager: GroupManager,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel(), ViewModelHistoryAdapter {

    fun getHistory() =purchaseManager.getAllHistory()
    override fun getPurchase(purchaseId: UUID) = purchaseManager.getById(purchaseId)
    override fun getPerson(personId: UUID) = groupManager.getPersonByIdLiveData(personId)
    override fun getShop(shopId: Int) = dictionaryRepository.getShopById(shopId)
    override fun getGroup(groupId: UUID) = groupManager.getGroupById(groupId)



}