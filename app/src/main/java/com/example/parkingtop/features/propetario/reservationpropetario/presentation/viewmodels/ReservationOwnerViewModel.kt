package com.example.parkingtop.features.propetario.reservationpropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.example.parkingtop.features.propetario.reservationpropetario.domain.usecases.GetOwnerReservationsUseCase
import com.example.parkingtop.features.propetario.reservationpropetario.domain.usecases.UpdateReservationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReservationOwnerState(
    val isLoading: Boolean = false,
    val reservations: List<ReservationOwner> = emptyList(),
    val error: String? = null,
    val selectedFilter: String = "Todas"
)

@HiltViewModel
class ReservationOwnerViewModel @Inject constructor(
    private val getOwnerReservationsUseCase: GetOwnerReservationsUseCase,
    private val updateReservationStatusUseCase: UpdateReservationStatusUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ReservationOwnerState())
    val state: State<ReservationOwnerState> = _state

    init {
        loadReservations()
    }

    fun loadReservations(filter: String = _state.value.selectedFilter) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, selectedFilter = filter)
            
            val statusParam = when (filter) {
                "Pendientes" -> "pending"
                "Confirmadas" -> "confirmed"
                "Canceladas" -> "cancelled"
                else -> null
            }

            getOwnerReservationsUseCase(statusParam).fold(
                onSuccess = { reservations ->
                    _state.value = _state.value.copy(isLoading = false, reservations = reservations)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
            )
        }
    }

    fun updateStatus(id: String, status: ReservationStatus) {
        viewModelScope.launch {
            updateReservationStatusUseCase(id, status).fold(
                onSuccess = {
                    loadReservations()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(error = error.message)
                }
            )
        }
    }
}
