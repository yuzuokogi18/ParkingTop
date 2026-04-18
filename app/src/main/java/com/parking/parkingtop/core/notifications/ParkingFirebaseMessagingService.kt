package com.parking.parkingtop.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.di.navigation.DeepLinkHandler
import com.parking.parkingtop.features.notifications.data.repositories.FcmTokenRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ParkingFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var tokenDataStore: TokenDataStore
    @Inject lateinit var fcmTokenRepository: FcmTokenRepository
    @Inject lateinit var deepLinkHandler: DeepLinkHandler  // ← AÑADIR

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data

        val title = message.notification?.title ?: data["title"] ?: "Notificación"
        val body  = message.notification?.body  ?: data["body"]  ?: ""
        val type  = data["type"] ?: "general"

        // ── AÑADIR: si es reservation_completed y app en foreground, navegar directo ──
        if (type == "reservation_completed") {
            val parkingLotId  = data["parkingLotId"]
            val reservationId = data["reservationId"]
            val parkingName   = data["parkingName"].orEmpty()
            val notifId       = data["notificationId"].orEmpty()

            if (!parkingLotId.isNullOrBlank() && !reservationId.isNullOrBlank()) {
                deepLinkHandler.emitReviewEvent(
                    parkingLotId   = parkingLotId,
                    reservationId  = reservationId,
                    parkingName    = parkingName,
                    notificationId = notifId
                )

                // Solo muestra notificación del sistema si está en background
                if (!isAppInForeground()) {
                    notificationHelper.show(
                        title        = title,
                        message      = body,
                        type         = type,
                        deepLinkData = data
                    )
                }
                return  // ← no continúes al show() genérico
            }
        }

        notificationHelper.show(title = title, message = body, type = type, deepLinkData = data)
    }

    // ── Helper foreground ─────────────────────────────────────────────────────
    private fun isAppInForeground(): Boolean {
        val info = android.app.ActivityManager.RunningAppProcessInfo()
        android.app.ActivityManager.getMyMemoryState(info)
        return info.importance ==
                android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }

    override fun onNewToken(token: String) {
        serviceScope.launch {
            try {
                val accessToken = tokenDataStore.accessToken.first()
                if (accessToken != null) fcmTokenRepository.registerToken(token)
            } catch (_: Exception) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}