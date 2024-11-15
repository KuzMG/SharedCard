package com.example.sharedcard.service.stomp

import com.example.sharedcard.database.entity.check.CheckEntity
import com.example.sharedcard.database.entity.user.UserEntity
import com.example.sharedcard.service.api.AuthApi
import com.example.sharedcard.service.dto.CheckResponse
import com.example.sharedcard.service.dto.CreateGroupResponse
import com.example.sharedcard.service.dto.DeleteResponse
import com.example.sharedcard.service.dto.SetUserStatusResponse
import com.example.sharedcard.service.dto.TargetResponse
import com.example.sharedcard.service.dto.UpdateGroupResponse
import com.example.sharedcard.service.dto.UpdateUserResponse
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

    fun connect(userId: UUID, userPassword: String) {
        val headers = listOf(
            StompHeader(AuthApi.HEADER_ID_USER, userId.toString()),
            StompHeader(AuthApi.HEADER_PASSWORD_USER, userPassword)
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


    fun sendCheck(check: CheckEntity, groupId: UUID, userId: UUID, password: String): Completable {
        val response = CheckResponse(check, groupId)
        connect(userId, password)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(CHECK_PATH_SEND, jsonResponse)
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

    fun deleteCheck(
        idCheck: UUID,
        userId: UUID,
        groupId: UUID,
        password: String
    ): Completable {
        connect(userId, password)
        val response = DeleteResponse(idCheck, groupId, userId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(CHECK_DELETE_PATH_SEND, jsonResponse)
    }

    fun deleteTarget(
        idTarget: UUID,
        userId: UUID,
        groupId: UUID,
        password: String
    ): Completable {
        connect(userId, password)
        val response = DeleteResponse(idTarget, groupId, userId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(TARGET_DELETE_PATH_SEND, jsonResponse)
    }

    fun createGroup(name: String, userId: UUID, password: String, pic: ByteArray): Completable {
        connect(userId, password)
        val response = CreateGroupResponse(name, userId, pic)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(CREATE_GROUP_PATH_SEND, jsonResponse)
    }

    fun updateGroup(
        groupId: UUID,
        name: String,
        userId: UUID,
        password: String
    ): Completable {
        connect(userId, password)
        val response = UpdateGroupResponse(name, groupId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(UPDATE_GROUP_PATH_SEND, jsonResponse)
    }

    fun updateUser(
        user: UserEntity,
        userId: UUID,
        password: String
    ): Completable{
        connect(userId, password)
        val response = UpdateUserResponse(user)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(UPDATE_USER_PATH_SEND, jsonResponse)
    }

    fun deleteGroup(
        groupId: UUID,
        userId: UUID,
        password: String
    ): Completable {
        connect(userId, password)

        val response = DeleteResponse(groupId, groupId, userId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(DELETE_GROUP_PATH_SEND, jsonResponse)
    }

    fun deleteUser(userDelId: UUID, groupId: UUID, userId: UUID, password: String): Completable {
        connect(userId, password)
        val response = DeleteResponse(userDelId, groupId, userId)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(DELETE_USER_PATH_SEND, jsonResponse)
    }

    fun setUserStatus(
        userAdminId: UUID,
        groupId: UUID,
        status: Int,
        userId: UUID,
        password: String
    ): Completable {
        connect(userId, password)
        val response = SetUserStatusResponse(userAdminId, groupId, userId,status)
        val jsonResponse = Gson().toJson(response)
        return stompClient.send(MAKE_USER_ADMIN_PATH_SEND, jsonResponse)
    }


    companion object {
        private const val SYNC_FULL_PATH_SUBSCRIBE = "/app/synchronization/full/"
        private const val SYNC_PATH_SUBSCRIBE = "/app/synchronization/"
        private const val SYNC_DELETE_PATH_SUBSCRIBE = "/app/synchronization/delete/"

        private const val UPDATE_USER_PATH_SEND = "/server/user/update"

        private const val CREATE_GROUP_PATH_SEND = "/server/group/create"
        private const val UPDATE_GROUP_PATH_SEND = "/server/group/update"
        private const val DELETE_GROUP_PATH_SEND = "/server/group/delete"
        private const val DELETE_USER_PATH_SEND = "/server/group/user/delete"
        private const val MAKE_USER_ADMIN_PATH_SEND = "/server/group/user/admin"

        private const val CHECK_PATH_SEND = "/server/check"
        private const val TARGET_PATH_SEND = "/server/target"
        private const val CHECK_DELETE_PATH_SEND = "/server/check/delete"
        private const val TARGET_DELETE_PATH_SEND = "/server/target/delete"
        private fun fullSyncFullPath(userId: UUID) = SYNC_FULL_PATH_SUBSCRIBE + userId
        private fun fullSyncPath(userId: UUID) = SYNC_PATH_SUBSCRIBE + userId
        private fun fullSyncDeletePath(userId: UUID) = SYNC_DELETE_PATH_SUBSCRIBE + userId
    }
}