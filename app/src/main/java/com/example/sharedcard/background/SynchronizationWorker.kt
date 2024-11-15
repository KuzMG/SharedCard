package com.example.sharedcard.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sharedcard.repository.AccountManager
import com.example.sharedcard.repository.AuthManager
import com.example.sharedcard.service.dto.AccountDeleteResponse
import com.example.sharedcard.service.dto.AccountResponse
import com.example.sharedcard.service.stomp.StompHelper
import com.example.sharedcard.util.appComponent
import com.google.gson.Gson
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.UUID
import javax.inject.Inject


class SynchronizationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(
    context,
    workerParams
) {
    @Inject
    lateinit var stompHelper: StompHelper

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var accountManager: AccountManager

    init {
        context.appComponent.inject(this)
    }

    //    private val synchronizationApi = SharedCardService.getInstance().create(SynchronizationApi::class.java)
//    private val dictionaryRepository = (applicationContext as SharedCardApp).getDictionaryRepository()
    override suspend fun doWork(): Result {
        val user = accountManager.getUserAccount()
        Log.d(SynchronizationWorker::class.simpleName, "doWork")
        stompHelper.lifecycle().subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    subscribeStomp(user.id)
                }

                LifecycleEvent.Type.ERROR -> Log.e(
                    SynchronizationWorker::class.simpleName,
                    "Error",
                    lifecycleEvent.exception
                )

                LifecycleEvent.Type.CLOSED -> Log.d(
                    SynchronizationWorker::class.simpleName,
                    "Stomp connection closed"
                )

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Log.d(
                    SynchronizationWorker::class.simpleName,
                    lifecycleEvent.message
                )
            }
        }


        stompHelper.connect(user.id, user.password)

        return Result.success()
    }

    private fun subscribeStomp(userId: UUID) {
        stompHelper.subscribeOnSyncFull(userId).subscribe { msg ->
            val accountResponse = Gson().fromJson(msg.payload, AccountResponse::class.java)
            Log.d("TAG",accountResponse.toString())
            authManager.dropAndSaveAccountResponse(accountResponse)
        }
        stompHelper.subscribeOnSync(userId).subscribe { msg ->
            val accountResponse = Gson().fromJson(msg.payload, AccountResponse::class.java)
            authManager.saveAccountResponse(accountResponse,false)
        }
        stompHelper.subscribeOnSyncDelete(userId).subscribe { msg ->
            val accountDeleteResponse =
                Gson().fromJson(msg.payload, AccountDeleteResponse::class.java)
            authManager.deleteAccountResponse(accountDeleteResponse)
        }
    }
}



