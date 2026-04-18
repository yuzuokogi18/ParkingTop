package com.parking.parkingtop.features.propetario.reservationpropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases.CheckInReservationUseCase
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases.CheckOutReservationUseCase
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases.ConfirmCashPaymentUseCase
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases.GetOwnerReservationsUseCase
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
    private val confirmCashPaymentUseCase: ConfirmCashPaymentUseCase,
    private val checkInUseCase: CheckInReservationUseCase,
    private val checkOutUseCase: CheckOutReservationUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ReservationOwnerState())
    val state: State<ReservationOwnerState> = _state

    init {
        loadReservations()
    }

    fun loadReservations(filter: String = _state.value.selectedFilter) {
        // Capturar fuera del suspend block para evitar leer estado intermedio
        val activeFilter = filter
        val statusParam = when (activeFilter) {
            "Pendientes"  -> "pending"
            "Confirmadas" -> "confirmed"
            "Canceladas"  -> "cancelled"
            else          -> null
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, selectedFilter = activeFilter)

            getOwnerReservationsUseCase(status = statusParam).fold(
                onSuccess = { reservations ->
                    _state.value = _state.value.copy(
                        isLoading    = false,
                        reservations = reservations,
                        error        = null
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error     = error.message
                    )
                }
            )
        }
    }

    fun confirmCashPayment(id: String) {
        val currentFilter = _state.value.selectedFilter
        viewModelScope.launch {
            confirmCashPaymentUseCase(id).fold(
                onSuccess = { loadReservations(currentFilter) },
                onFailure = { _state.value = _state.value.copy(error = it.message) }
            )
        }
    }

    fun checkIn(id: String) {
        val currentFilter = _state.value.selectedFilter
        viewModelScope.launch {
            checkInUseCase(id).fold(
                onSuccess = { loadReservations(currentFilter) },
                onFailure = { _state.value = _state.value.copy(error = it.message) }
            )
        }
    }

    fun checkOut(id: String) {
        val currentFilter = _state.value.selectedFilter
        viewModelScope.launch {
            checkOutUseCase(id).fold(
                onSuccess = { loadReservations(currentFilter) },
                onFailure = { _state.value = _state.value.copy(error = it.message) }
            )
        }
    }
}