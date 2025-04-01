package com.example.sharedcard.service.stomp

import com.example.sharedcard.database.entity.basket.BasketEntity
import com.example.sharedcard.database.entity.history.HistoryEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.database.entity.person.PersonEntity
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.dto.BasketResponse
import com.example.sharedcard.service.dto.PurchaseResponse
import com.example.sharedcard.service.dto.DeleteResponse
import com.example.sharedcard.service.dto.HistoryResponse
import com.example.sharedcard.service.dto.SetUserStatusResponse
import com.example.sharedcard.service.dto.TargetResponse
import com.example.sharedcard.service.dto.UpdateGroupResponse
import com.example.sharedcard.service.dto.UpdatePersonResponse
import com.google.gson.Gson
import com.project.shared_card.database.dao.target.TargetEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.UUID
import javax.inject.Inject

class StompHelper @Inject constructor(private val stompClient: StompClient) {

    fun lifecycle(): Flowable<LifecycleEvent> = stompClient.lifecycle()

    fun connect(personId: UUID, password: String) {
        val headers = listOf(
            StompHeader(AuthApi.HEADER_ID_PERSON, personId.toString()),
            StompHeader(AuthApi.HEADER_PASSWORD_PERSON, password)
        )
        if (!stompClient.isConnected) {
            stompClient.connect(headers)
        }
    }

    fun subscribeOnSyncFull(userId: UUID): Flowable<StompMessage> =
        stompClient.topic(fullSyncFullPath(userId))

    fun subscribeOnSync(userId: UUID): Flowable<StompMessage> =
        stompClient.topic(fullSyncPath(userId))

    fun subscribeOnSyncDelete(userId: UUID): Flowable<StompMessage> =
        stompClient.topic(fullSyncDeletePath(userId))


    fun sendPurchase(purchase: PurchaseEntity, groupId: UUID, personId: UUID, password: String): Completable {
        val response = PurchaseResponse(purchase, groupId)
        connect(personId, password)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(PURCHASE_PATH_SEND, jsonResponse)
    }

    fun sendTarget(
        target: TargetEntity,
        groupId: UUID,
        userId: UUID,
        password: String
    ): Completable {
        connect(userId, password)
        val response = TargetResponse(target, groupId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(TARGET_PATH_SEND, jsonResponse)
    }

    fun deletePurchase(
        purchaseId: UUID,
        personId: UUID,
        groupId: UUID,
        password: String
    ): Completable {
        connect(personId, password)
        val response = DeleteResponse(purchaseId, groupId, personId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(PURCHASE_DELETE_PATH_SEND, jsonResponse)
    }




    fun updateGroup(
        groupId: UUID,
        name: String,
        personId: UUID,
        password: String
    ): Completable {
        connect(personId, password)
        val response = UpdateGroupResponse(name, groupId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(UPDATE_GROUP_PATH_SEND, jsonResponse)
    }

    fun updatePerson(
        person: PersonEntity,
        personId: UUID,
        password: String
    ): Completable{
        connect(personId, password)
        val response = UpdatePersonResponse(person)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(UPDATE_PERSON_PATH_SEND, jsonResponse)
    }

    fun deleteGroup(
        groupId: UUID,
        personId: UUID,
        password: String
    ): Completable {
        connect(personId, password)

        val response = DeleteResponse(groupId, groupId, personId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(DELETE_GROUP_PATH_SEND, jsonResponse)
    }

    fun deletePerson(personDelId: UUID, groupId: UUID, personId: UUID, password: String): Completable {
        connect(personId, password)
        val response = DeleteResponse(personDelId, groupId, personId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(DELETE_PERSON_PATH_SEND, jsonResponse)
    }

    fun setPersonStatus(
        personAdminId: UUID,
        groupId: UUID,
        status: Int,
        personId: UUID,
        password: String
    ): Completable {
        connect(personId, password)
        val response = SetUserStatusResponse(personAdminId, groupId, personId,status)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(MAKE_PERSON_ADMIN_PATH_SEND, jsonResponse)
    }

    fun sendBasket(
        basket: BasketEntity,
        groupId: UUID,
        personId: UUID,
        password: String
    ): Completable {
        val response = BasketResponse(basket, groupId)
        connect(personId, password)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(BASKET_PATH_SEND, jsonResponse)
    }

    fun deleteBasket(basketId: UUID, personId: UUID, groupId: UUID, password: String): Completable {
        connect(personId, password)
        val response = DeleteResponse(basketId, groupId, personId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(DELETE_BASKET_PATH_SEND, jsonResponse)
    }

    fun sendHistory(
        history: HistoryEntity,
        groupId: UUID,
        personId: UUID,
        password: String
    ): Completable {
        connect(personId, password)
        val response = HistoryResponse(history, groupId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(HISTORY_PATH_SEND, jsonResponse)
    }


    companion object {
        private const val SYNC_FULL_PATH_SUBSCRIBE = "/app/synchronization/full/"
        private const val SYNC_PATH_SUBSCRIBE = "/app/synchronization/"
        private const val SYNC_DELETE_PATH_SUBSCRIBE = "/app/synchronization/delete/"

        private const val UPDATE_PERSON_PATH_SEND = "/server/person/update"

        private const val UPDATE_GROUP_PATH_SEND = "/server/group/update"
        private const val DELETE_GROUP_PATH_SEND = "/server/group/delete"
        private const val DELETE_PERSON_PATH_SEND = "/server/group/person/delete"
        private const val DELETE_BASKET_PATH_SEND = "/server/basket/delete"

        private const val MAKE_PERSON_ADMIN_PATH_SEND = "/server/group/person/admin"

        private const val HISTORY_PATH_SEND = "/server/history"
        private const val BASKET_PATH_SEND = "/server/basket"
        private const val PURCHASE_PATH_SEND = "/server/purchase"
        private const val TARGET_PATH_SEND = "/server/target"
        private const val PURCHASE_DELETE_PATH_SEND = "/server/purchase/delete"
        private const val TARGET_DELETE_PATH_SEND = "/server/target/delete"
        private fun fullSyncFullPath(personId: UUID) = SYNC_FULL_PATH_SUBSCRIBE + personId
        private fun fullSyncPath(personId: UUID) = SYNC_PATH_SUBSCRIBE + personId
        private fun fullSyncDeletePath(personId: UUID) = SYNC_DELETE_PATH_SUBSCRIBE + personId
    }
}