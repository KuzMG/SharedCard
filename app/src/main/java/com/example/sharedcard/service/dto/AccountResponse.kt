package com.example.sharedcard.service.dto

import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import com.project.shared_card.database.dao.target.TargetEntity

data class AccountResponse(
    val persons: List<PersonEntity>,
    val groups: List<GroupEntity>,
    val groupPersons: List<GroupPersonsEntity>,
    val purchases: List<PurchaseEntity>,
    val baskets: List<BasketEntity>,
    val histories: List<HistoryEntity>
)