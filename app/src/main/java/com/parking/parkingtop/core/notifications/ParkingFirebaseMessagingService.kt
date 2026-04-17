package com.parking.parkingtop.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.parking.parkingtop.core.datastore.TokenDataStore
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

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // ── Mensaje recibido ──────────────────────────────────────────────────────
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data

        val title = message.notification?.title
            ?: data["title"]
            ?: "Notificación"

        val body = message.notification?.body
            ?: data["body"]
            ?: ""

        val type = data["type"] ?: "general"

        // Importante: mandar TODO el data, incluido type
        notificationHelper.show(
            title = title,
            message = body,
            type = type,
            deepLinkData = data
        )
    }

    // ── Token rotado por Firebase ─────────────────────────────────────────────
    override fun onNewToken(token: String) {
        serviceScope.launch {
            try {
                val accessToken = tokenDataStore.accessToken.first()
                if (accessToken != null) {
                    // Registra el nuevo token en el backend
                    fcmTokenRepository.registerToken(token)
                }
            } catch (_: Exception) { }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}