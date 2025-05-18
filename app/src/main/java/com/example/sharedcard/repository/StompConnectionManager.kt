package com.example.sharedcard.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sharedcard.database.entity.person.PersonAccountEntity
import com.example.sharedcard.database.entity.purchase.PurchaseEntity
import com.example.sharedcard.service.dto.AccountDeleteResponse
import com.example.sharedcard.service.dto.AccountResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.UUID
import javax.inject.Inject

class StompConnectionManager @Inject constructor(
    private val stompHelper: StompHelper,
    private val authManager: AuthManager
) {
    private val _connectionLiveData = MutableLiveData<ConnectionState>()
    val connectionLiveData: LiveData<ConnectionState>
        get() = _connectionLiveData
    private var connectDispose: Disposable
    private lateinit var syncDeleteDispose: Disposable
    private lateinit var syncDispose: Disposable
    private lateinit var syncFullDispose: Disposable
    private var account: PersonAccountEntity? = null
    init {
        connectDispose = stompHelper.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    _connectionLiveData.postValue(ConnectionState.CONNECTING)
                    account?.let { account ->
                        subscribeStomp(account.id)
                    }
                }

                LifecycleEvent.Type.CLOSED -> _connectionLiveData.postValue(ConnectionState.DISCONNECTION)

                LifecycleEvent.Type.ERROR -> _connectionLiveData.postValue(
                    ConnectionState.ERROR(
                        lifecycleEvent.exception
                    )
                )

                else -> Unit
            }
        }
    }
    fun connect(_account: PersonAccountEntity) {
        account = _account
        stompHelper.connect(account!!.id, account!!.password)
    }

    fun disconnect() {
        try {
            connectDispose.dispose()
            syncDeleteDispose.dispose()
            syncDispose.dispose()
            syncFullDispose.dispose()
        } catch (e:Exception){

        }

    }

    private fun subscribeStomp(userId: UUID) {
        syncFullDispose = stompHelper.subscribeOnSyncFull(userId).subscribe { msg ->
            val accountResponse = Gson().fromJson(msg.payload, AccountResponse::class.java)
            authManager.dropAndSaveAccountResponse(accountResponse)
        }
        syncDispose = stompHelper.subscribeOnSync(userId).subscribe { msg ->
            val accountResponse = Gson().fromJson(msg.payload, AccountResponse::class.java)
            if (accountResponse.purchases.isNotEmpty()) {
                _connectionLiveData.postValue(ConnectionState.NEW_PURCHASE(accountResponse.purchases))
            }
            authManager.saveAccountResponse(accountResponse, false)
        }
        syncDeleteDispose = stompHelper.subscribeOnSyncDelete(userId).subscribe { msg ->
            val accountDeleteResponse =
                Gson().fromJson(msg.payload, AccountDeleteResponse::class.java)
            authManager.deleteAccountResponse(accountDeleteResponse)
        }
    }

    sealed class ConnectionState {
        data object CONNECTING : ConnectionState()
        data object DISCONNECTION : ConnectionState()
        class ERROR(val e: Exception) : ConnectionState()
        class NEW_PURCHASE(val purchases: List<PurchaseEntity>) : ConnectionState()
    }
}