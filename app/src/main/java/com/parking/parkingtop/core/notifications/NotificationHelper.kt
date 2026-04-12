package com.parking.parkingtop.core.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.parking.parkingtop.MainActivity
import com.parking.parkingtop.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val manager = NotificationManagerCompat.from(context)
    private var notificationId = 1000

    fun show(
        title: String,
        message: String,
        type: String,
        deepLinkData: Map<String, String> = emptyMap()
    ) {
        val channel = channelForType(type)

        // Intent que abre la app con los datos del deep link
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            deepLinkData.forEach { (k, v) -> putExtra(k, v) }
            putExtra("notification_type", type)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Permiso POST_NOTIFICATIONS ya declarado en el Manifest
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            manager.notify(notificationId++, notification)
        }
    }

    private fun channelForType(type: String) = when {
        type.startsWith("reservation") || type.startsWith("overtime") -> NotificationChannels.RESERVATIONS
        type.startsWith("payment")                                     -> NotificationChannels.PAYMENTS
        else                                                           -> NotificationChannels.GENERAL
    }
}