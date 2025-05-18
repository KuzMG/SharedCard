package com.example.sharedcard.ui.history.adapter

import androidx.lifecycle.LiveData
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.database.entity.shop.ShopEntity
import java.util.UUID

interface ViewModelHistoryAdapter {


    fun getPurchase(purchaseId: UUID): LiveData<Purchase>
    fun getPerson(personId: UUID):LiveData<PersonEntity>
    fun getShop(shopId: Int): LiveData<ShopEntity>
    fun getGroup(groupId: UUID):  LiveData<GroupEntity>
}