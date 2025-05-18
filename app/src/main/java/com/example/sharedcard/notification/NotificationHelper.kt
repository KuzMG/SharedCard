package com.example.sharedcard.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sharedcard.R
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object NotificationHelper {
    const val CHANNEL_ID = "channel"
    private const val CHANNEL_NAME = "Покупки"
    private const val CHANNEL_DESCRIPTION = "отслеживание покупок пользователей"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun showUserProductNotification(
        context: Context,
        items: List<NotificationItem>
    ) {
        val intent = Intent(context, NavigationDrawerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        CoroutineScope(Dispatchers.IO).launch {
            items.forEach { item ->
                val bitmap = Picasso.get().load(item.picUrl).resize(64, 64).centerInside().get()
                withContext(Dispatchers.Main) {
                    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon_ok)
                        .setLargeIcon(bitmap)
                        .setContentTitle(item.title)
                        .setContentText(item.text)
                        .setSubText(item.subText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                    val notificationId = System.currentTimeMillis().toInt()
                    NotificationManagerCompat.from(context).notify(notificationId, builder.build())
                }
            }
        }
    }
}