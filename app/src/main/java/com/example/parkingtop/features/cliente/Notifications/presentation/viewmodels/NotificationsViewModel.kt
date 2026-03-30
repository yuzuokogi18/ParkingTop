package com.example.parkingtop.features.cliente.Notifications.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.cliente.Notifications.domain.entities.AppNotification
import com.example.parkingtop.features.cliente.Notifications.domain.usecases.GetNotificationsUseCase
import com.example.parkingtop.features.cliente.Notifications.domain.usecases.GetUnreadCountUseCase
import com.example.parkingtop.features.cliente.Notifications.domain.usecases.MarkAllReadUseCase
import com.example.parkingtop.features.cliente.Notifications.domain.usecases.MarkAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<AppNotification> = emptyList(),
    val unreadCount: Int   = 0,
    val isLoading: Boolean = true,
    val error: String?     = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotifications: GetNotificationsUseCase,
    private val getUnreadCount:   GetUnreadCountUseCase,
    private val markAsRead:       MarkAsReadUseCase,
    private val markAllRead:      MarkAllReadUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsUiState())
    val state: StateFlow<NotificationsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val notifResult  = getNotifications.execute()
            val countResult  = getUnreadCount.execute()

            _state.value = _state.value.copy(
                isLoading     = false,
                notifications = notifResult.getOrElse { emptyList() },
                unreadCount   = countResult.getOrElse { 0 },
                error         = notifResult.exceptionOrNull()?.message
            )
        }
    }

    fun onMarkAsRead(id: String) {
        viewModelScope.launch {
            markAsRead.execute(id)
            // Actualiza localmente sin recargar toda la lista
            _state.value = _state.value.copy(
                notifications = _state.value.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                },
                unreadCount = (_state.value.unreadCount - 1).coerceAtLeast(0)
            )
        }
    }

    fun onMarkAllAsRead() {
        viewModelScope.launch {
            markAllRead.execute()
            _state.value = _state.value.copy(
                notifications = _state.value.notifications.map { it.copy(isRead = true) },
                unreadCount   = 0
            )
        }
    }
}