package com.parking.parkingtop.features.notifications.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.notifications.domain.entities.Notification
import com.parking.parkingtop.features.notifications.domain.usecases.GetNotificationsUseCase
import com.parking.parkingtop.features.notifications.domain.usecases.GetUnreadCountUseCase
import com.parking.parkingtop.features.notifications.domain.usecases.MarkAllAsReadUseCase
import com.parking.parkingtop.features.notifications.domain.usecases.MarkAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int                  = 0,
    val isLoading: Boolean                = false,
    val isRefreshing: Boolean             = false,
    val error: String?                    = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUnreadCountUseCase: GetUnreadCountUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val markAllAsReadUseCase: MarkAllAsReadUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsUiState())
    val state: StateFlow<NotificationsUiState> = _state.asStateFlow()

    // Expone el unreadCount directamente para el badge en TopAppBar
    val unreadCount: StateFlow<Int> = MutableStateFlow(0).also { flow ->
        viewModelScope.launch {
            _state.collect { flow.value = it.unreadCount }
        }
    }

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // Carga notificaciones y contador en paralelo
            val notificationsResult = getNotificationsUseCase.execute()
            val countResult         = getUnreadCountUseCase.execute()

            _state.value = _state.value.copy(
                notifications = notificationsResult.getOrDefault(emptyList()),
                unreadCount   = countResult.getOrDefault(0),
                isLoading     = false,
                error         = notificationsResult.exceptionOrNull()?.message
            )
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            val result = getNotificationsUseCase.execute()
            val count  = getUnreadCountUseCase.execute()
            _state.value = _state.value.copy(
                notifications = result.getOrDefault(_state.value.notifications),
                unreadCount   = count.getOrDefault(_state.value.unreadCount),
                isRefreshing  = false
            )
        }
    }

    // ✅ Refresca solo el contador — útil para el badge en HomeScreen
    fun refreshUnreadCount() {
        viewModelScope.launch {
            getUnreadCountUseCase.execute().onSuccess { count ->
                _state.value = _state.value.copy(unreadCount = count)
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            markAsReadUseCase.execute(id).onSuccess {
                _state.value = _state.value.copy(
                    notifications = _state.value.notifications.map { n ->
                        if (n.id == id) n.copy(isRead = true) else n
                    },
                    unreadCount = maxOf(0, _state.value.unreadCount - 1)
                )
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllAsReadUseCase.execute().onSuccess {
                _state.value = _state.value.copy(
                    notifications = _state.value.notifications.map { it.copy(isRead = true) },
                    unreadCount   = 0
                )
            }
        }
    }
}