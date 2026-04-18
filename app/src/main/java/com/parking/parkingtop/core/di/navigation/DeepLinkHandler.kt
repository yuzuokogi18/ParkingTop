package com.parking.parkingtop.core.di.navigation

import android.content.Intent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkHandler @Inject constructor() {

    // En DeepLinkHandler
    private val _events = MutableSharedFlow<DeepLinkEvent>(
        replay = 1,               // ← era 0, ahora 1
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<DeepLinkEvent> = _events.asSharedFlow()

    private var pendingEvent: DeepLinkEvent.OpenReview? = null
    private var isLoggedIn: Boolean = false

    // Para deduplicar pushes repetidos del mismo evento
    private val handledKeys = LinkedHashSet<String>()
    private val maxHandledKeys = 100

    private var currentRole: String = ""

    // Cambiar la firma de updateAuthState
    fun updateAuthState(loggedIn: Boolean, role: String = "") {
        isLoggedIn = loggedIn
        currentRole = role
        if (loggedIn && role == "customer") flushPendingEvent()
    }

    fun handle(intent: Intent?): Boolean {
        intent ?: return false

        val type = intent.getStringExtra("type")
            ?: intent.getStringExtra("notification_type")

        val action = intent.getStringExtra("action")
        val screen = intent.getStringExtra("screen")
        val reservationId = intent.getStringExtra("reservationId")
        val parkingLotId = intent.getStringExtra("parkingLotId")
        val parkingName = intent.getStringExtra("parkingName").orEmpty()
        val notificationId = intent.getStringExtra("notificationId").orEmpty()

        val isReviewTrigger =
            type == "reservation_completed" ||
                    action == "open_review" ||
                    screen == "review"

        if (!isReviewTrigger || reservationId.isNullOrBlank() || parkingLotId.isNullOrBlank()) {
            return false
        }

        val dedupeKey = buildString {
            append(reservationId)
            append("|")
            append(if (notificationId.isBlank()) "no-notification-id" else notificationId)
        }

        if (handledKeys.contains(dedupeKey)) return false
        markHandled(dedupeKey)

        val event = DeepLinkEvent.OpenReview(
            parkingLotId = parkingLotId,
            reservationId = reservationId,
            parkingName = parkingName
        )

        if (isLoggedIn) {
            _events.tryEmit(event)
        } else {
            pendingEvent = event
        }

        // Limpiar extras para evitar re-procesar el mismo Intent
        intent.removeExtra("type")
        intent.removeExtra("notification_type")
        intent.removeExtra("action")
        intent.removeExtra("screen")
        intent.removeExtra("reservationId")
        intent.removeExtra("parkingLotId")
        intent.removeExtra("parkingName")
        intent.removeExtra("notificationId")

        return true
    }

    fun flushPendingEvent() {
        val event = pendingEvent ?: return
        if (!isLoggedIn) return
        _events.tryEmit(event)
        pendingEvent = null
    }

    fun clearPendingEvent() {
        pendingEvent = null
    }

    private fun markHandled(key: String) {
        handledKeys.add(key)
        if (handledKeys.size > maxHandledKeys) {
            val first = handledKeys.firstOrNull()
            if (first != null) handledKeys.remove(first)
        }
    }

    fun emitReviewEvent(
        parkingLotId: String,
        reservationId: String,
        parkingName: String,
        notificationId: String = ""
    ) {
        // ← AÑADIR: ignorar si el usuario activo es owner
        if (currentRole == "owner") return

        val dedupeKey = "$reservationId|${notificationId.ifBlank { "no-notification-id" }}"
        if (handledKeys.contains(dedupeKey)) return
        markHandled(dedupeKey)

        val event = DeepLinkEvent.OpenReview(
            parkingLotId  = parkingLotId,
            reservationId = reservationId,
            parkingName   = parkingName
        )

        if (isLoggedIn) _events.tryEmit(event) else pendingEvent = event
    }

    // En DeepLinkHandler
    fun clearLastEvent() {
        _events.resetReplayCache()
    }
}