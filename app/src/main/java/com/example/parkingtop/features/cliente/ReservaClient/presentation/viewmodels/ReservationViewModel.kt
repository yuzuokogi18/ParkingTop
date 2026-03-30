package com.example.parkingtop.features.cliente.ReservaClient.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.ceil

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val licensePlate: String
)

data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val basePricePerHour: Double
)

data class ReservationState constructor(
    val parkingLot: ParkingLot? = null,
    val selectedVehicle: Vehicle? = null,
    val vehicles: List<Vehicle> = emptyList(),

    // Fecha y hora de entrada
    val entryDate: LocalDate = LocalDate.now(),
    val entryTime: LocalTime = LocalTime.now(),

    // Fecha y hora de salida
    val exitDate: LocalDate = LocalDate.now(),
    val exitTime: LocalTime = LocalTime.now().plusHours(4),

    // Cálculo de precios
    val hours: Int = 4,
    val baseCost: Double = 0.0,
    val additionalTime: Double = 0.0,
    val discounts: Double = 0.0,
    val total: Double = 0.0,

    // UI State
    val isLoading: Boolean = false,
    val error: String? = null,
    val reservationSuccess: Boolean = false,
    val paymentUrl: String? = null,

    // Payment
    val selectedPaymentMethod: String = "Tarjeta de Crédito"
)

@HiltViewModel
class ReservationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
    // Agregar use cases cuando estén disponibles
) : ViewModel() {
    private val _state = mutableStateOf(ReservationState())
    val state: State<ReservationState> = _state

    init {
        val parkingId = savedStateHandle.get<String>("parkingId")
        loadData(parkingId)
    }

    private fun loadData(parkingId: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // TODO: Cargar datos del estacionamiento desde API
            // Datos de ejemplo por ahora
            val mockParkingLot = ParkingLot(
                id = parkingId ?: "1",
                name = "Parking Central",
                address = "Av. Principal 123",
                basePricePerHour = 2.0
            )

            val mockVehicles = listOf(
                Vehicle(
                    id = "1",
                    brand = "Toyota",
                    model = "Corolla",
                    licensePlate = "ABC-123"
                ),
                Vehicle(
                    id = "2",
                    brand = "Honda",
                    model = "Civic",
                    licensePlate = "XYZ-789"
                )
            )

            _state.value = _state.value.copy(
                parkingLot = mockParkingLot,
                vehicles = mockVehicles,
                selectedVehicle = mockVehicles.firstOrNull(),
                isLoading = false
            )

            calculatePrice()
        }
    }

    fun selectVehicle(vehicle: Vehicle) {
        _state.value = _state.value.copy(selectedVehicle = vehicle)
    }

    fun updateEntryDate(date: LocalDate) {
        _state.value = _state.value.copy(entryDate = date)
        calculatePrice()
    }

    fun updateEntryTime(time: LocalTime) {
        _state.value = _state.value.copy(entryTime = time)
        calculatePrice()
    }

    fun updateExitDate(date: LocalDate) {
        _state.value = _state.value.copy(exitDate = date)
        calculatePrice()
    }

    fun updateExitTime(time: LocalTime) {
        _state.value = _state.value.copy(exitTime = time)
        calculatePrice()
    }

    private fun calculatePrice() {
        val entry = LocalDateTime.of(_state.value.entryDate, _state.value.entryTime)
        val exit = LocalDateTime.of(_state.value.exitDate, _state.value.exitTime)

        // Calcular diferencia en minutos
        val minutes = ChronoUnit.MINUTES.between(entry, exit)

        if (minutes <= 0) {
            _state.value = _state.value.copy(
                hours = 0,
                baseCost = 0.0,
                additionalTime = 0.0,
                total = 0.0
            )
            return
        }

        // Calcular horas (redondeando hacia arriba)
        val hours = ceil(minutes / 60.0).toInt()

        val pricePerHour = _state.value.parkingLot?.basePricePerHour ?: 2.0
        val baseCost = hours * pricePerHour

        // Por ahora no hay tiempo adicional ni descuentos
        val additionalTime = 0.0
        val discounts = 0.0
        val total = baseCost + additionalTime - discounts

        _state.value = _state.value.copy(
            hours = hours,
            baseCost = baseCost,
            additionalTime = additionalTime,
            discounts = discounts,
            total = total
        )
    }

    fun confirmReservation() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // Validaciones
                if (_state.value.selectedVehicle == null) {
                    _state.value = _state.value.copy(
                        error = "Selecciona un vehículo",
                        isLoading = false
                    )
                    return@launch
                }

                if (_state.value.total <= 0) {
                    _state.value = _state.value.copy(
                        error = "Tiempo de reserva inválido",
                        isLoading = false
                    )
                    return@launch
                }

                // TODO: Llamar al API para crear reserva
                // val response = createReservationUseCase(...)

                // Simulación por ahora
                kotlinx.coroutines.delay(1500)

                _state.value = _state.value.copy(
                    reservationSuccess = true,
                    paymentUrl = "https://mercadopago.com/payment/123456",
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Error al crear reserva",
                    isLoading = false
                )
            }
        }
    }

    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }

    fun resetSuccess() {
        _state.value = _state.value.copy(reservationSuccess = false)
    }

    // Formatters para mostrar en UI
    fun getFormattedEntryDate(): String {
        return _state.value.entryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun getFormattedExitDate(): String {
        return _state.value.exitDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun getFormattedEntryTime(): String {
        return _state.value.entryTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getFormattedExitTime(): String {
        return _state.value.exitTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}