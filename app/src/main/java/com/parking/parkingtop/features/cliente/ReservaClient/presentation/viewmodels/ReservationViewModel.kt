package com.parking.parkingtop.features.cliente.ReservaClient.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.ReservationResult
import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases.CreateReservationUseCase
import com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases.GetDefaultVehicleUseCase
import com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases.GetParkingSpotsUseCase
import com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases.GetVehiclesForReservationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ceil


private val mexicoZone = ZoneId.of("America/Mexico_City")

data class ReservationState(
    val parkingLot: ParkingLot?         = null,
    val selectedVehicle: Vehicle?       = null,
    val vehicles: List<Vehicle>         = emptyList(),
    val spots: List<ParkingSpot>        = emptyList(),
    val selectedSpot: ParkingSpot?      = null,

        val entryDate: LocalDate  = LocalDate.now(),
    val entryTime: LocalTime  = LocalTime.now(),
    val exitDate: LocalDate   = LocalDate.now(),
    val exitTime: LocalTime   = LocalTime.now().plusHours(2),

    // ✅ Ahora hours y minutes separados para mostrar en UI
    val hours: Int             = 0,
    val minutes: Int           = 0,    // minutos sobrantes (ej: 1h 30m → hours=1, minutes=30)
    val baseCost: Double       = 0.0,
    val additionalTime: Double = 0.0,
    val discounts: Double      = 0.0,
    val total: Double          = 0.0,

    val selectedPaymentMethod: String  = "mercadopago",

    val isLoading: Boolean         = false,
    val isLoadingVehicles: Boolean = false,
    val isLoadingSpots: Boolean    = false,
    val error: String?             = null,

    val reservationResult: ReservationResult? = null,
    val reservationSuccess: Boolean           = false,
    val paymentUrl: String?                   = null
)

@HiltViewModel
class ReservationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVehiclesUseCase: GetVehiclesForReservationUseCase,
    private val getDefaultVehicleUseCase: GetDefaultVehicleUseCase,
    private val getParkingSpotsUseCase: GetParkingSpotsUseCase,
    private val createReservationUseCase: CreateReservationUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ReservationState())
    val state: State<ReservationState> = _state

    // parkingId viene de los navArgs
    private val parkingId: String = savedStateHandle.get<String>("parkingIdClient") ?: ""

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Parking lot — precio real del estacionamiento
            // TODO: reemplazar con endpoint GET /v1/parkings/:id cuando se conecte el detalle
            val mockParkingLot = ParkingLot(
                id = parkingId,
                name = "Estacionamiento",
                address = "",
                basePricePerHour = 20.0,
                overtimeRatePerHour = 25.0
            )
            _state.value = _state.value.copy(parkingLot = mockParkingLot, isLoading = false)

            // Carga paralela de vehículos y espacios
            loadVehicles()
            loadSpots()
            calculatePrice()
        }
    }

    // ── Vehículos reales del API ──────────────────────────────────────────────
    private fun loadVehicles() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingVehicles = true)

            val defaultResult  = getDefaultVehicleUseCase.execute()
            val defaultVehicle = defaultResult.getOrNull()

            getVehiclesUseCase.execute().fold(
                onSuccess = { vehicles ->
                    val selected = defaultVehicle
                        ?: vehicles.find { it.isDefault }
                        ?: vehicles.firstOrNull()
                    _state.value = _state.value.copy(
                        vehicles          = vehicles,
                        selectedVehicle   = selected,
                        isLoadingVehicles = false
                    )
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(
                        error             = "Error al cargar vehículos: ${e.message}",
                        isLoadingVehicles = false
                    )
                }
            )
        }
    }

    // ── Espacios del estacionamiento ──────────────────────────────────────────
    private fun loadSpots() {
        if (parkingId.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingSpots = true)
            getParkingSpotsUseCase.execute(parkingId).fold(
                onSuccess = { spots ->
                    _state.value = _state.value.copy(
                        spots          = spots,
                        isLoadingSpots = false
                    )
                },
                onFailure = {
                    _state.value = _state.value.copy(isLoadingSpots = false)
                }
            )
        }
    }

    // ── Selección ─────────────────────────────────────────────────────────────
    fun selectVehicle(vehicle: Vehicle) {
        _state.value = _state.value.copy(selectedVehicle = vehicle)
    }

    fun selectSpot(spot: ParkingSpot?) {
        _state.value = _state.value.copy(selectedSpot = spot)
    }

    fun selectPaymentMethod(method: String) {
        _state.value = _state.value.copy(selectedPaymentMethod = method)
    }

    // ── Fechas y horas ────────────────────────────────────────────────────────
    fun updateEntryDate(date: LocalDate) { _state.value = _state.value.copy(entryDate = date); calculatePrice() }
    fun updateEntryTime(time: LocalTime) { _state.value = _state.value.copy(entryTime = time); calculatePrice() }
    fun updateExitDate(date: LocalDate)  { _state.value = _state.value.copy(exitDate  = date); calculatePrice() }
    fun updateExitTime(time: LocalTime) {
        val entry = LocalDateTime.of(_state.value.entryDate, _state.value.entryTime)
        var exit  = LocalDateTime.of(_state.value.exitDate, time)

        if (exit <= entry) {
            exit = exit.plusDays(1) // 👈 clave
        }

        _state.value = _state.value.copy(
            exitDate = exit.toLocalDate(),
            exitTime = exit.toLocalTime()
        )

        calculatePrice()
    }

    private fun calculatePrice() {
        val entry   = LocalDateTime.of(_state.value.entryDate, _state.value.entryTime)
        val exit    = LocalDateTime.of(_state.value.exitDate,  _state.value.exitTime)
        val totalMinutes = ChronoUnit.MINUTES.between(entry, exit)

        if (totalMinutes <= 0) {
            _state.value = _state.value.copy(
                hours    = 0,
                minutes  = 0,
                baseCost = 0.0,
                total    = 0.0
            )
            return
        }

        val pricePerHour   = _state.value.parkingLot?.basePricePerHour ?: 20.0
        val pricePerMinute = pricePerHour / 60.0

        // ✅ Cobra exactamente por los minutos seleccionados (sin redondear a hora completa)
        val baseCost = totalMinutes * pricePerMinute

        val displayHours   = (totalMinutes / 60).toInt()
        val displayMinutes = (totalMinutes % 60).toInt()

        _state.value = _state.value.copy(
            hours    = displayHours,
            minutes  = displayMinutes,
            baseCost = baseCost,
            total    = baseCost
        )
    }

    // ── Crear reserva real ────────────────────────────────────────────────────
    fun confirmReservation() {
        val s = _state.value

        if (s.selectedVehicle == null) {
            _state.value = s.copy(error = "Selecciona un vehículo")
            return
        }

        val entry        = LocalDateTime.of(s.entryDate, s.entryTime)
        val exit         = LocalDateTime.of(s.exitDate,  s.exitTime)
        val totalMinutes = ChronoUnit.MINUTES.between(entry, exit)

        if (totalMinutes <= 0) {
            _state.value = s.copy(error = "La hora de salida debe ser posterior a la entrada")
            return
        }

        // ✅ Mínimo 15 minutos (opcional — descomenta si lo necesitas)
        // if (totalMinutes < 15) {
        //     _state.value = s.copy(error = "La reserva mínima es de 15 minutos")
        //     return
        // }

        val mxZone   = ZoneId.of("America/Mexico_City")
        val entryZdt = ZonedDateTime.of(s.entryDate, s.entryTime, mxZone)
        val exitZdt  = ZonedDateTime.of(s.exitDate,  s.exitTime,  mxZone)

        val entryIso = entryZdt.withZoneSameInstant(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val exitIso  = exitZdt.withZoneSameInstant(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val request = CreateReservationRequest(
                parkingLotId  = parkingId,
                parkingSpotId = s.selectedSpot?.id,
                vehicleId     = s.selectedVehicle.id,
                startTime     = entryIso,
                endTime       = exitIso,
                paymentMethod = s.selectedPaymentMethod
            )

            createReservationUseCase.execute(request).fold(
                onSuccess = { result ->
                    _state.value = _state.value.copy(
                        isLoading          = false,
                        reservationResult  = result,
                        reservationSuccess = true,
                        paymentUrl         = result.paymentUrl
                    )
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error     = e.message ?: "Error al crear la reserva"
                    )
                }
            )
        }
    }

    fun resetError()   { _state.value = _state.value.copy(error = null) }
    fun resetSuccess() { _state.value = _state.value.copy(reservationSuccess = false, reservationResult = null) }

    fun getFormattedEntryDate() = _state.value.entryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    fun getFormattedExitDate()  = _state.value.exitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    fun getFormattedEntryTime() = _state.value.entryTime
        .format(DateTimeFormatter.ofPattern("hh:mm a", Locale("es", "MX")))

    fun getFormattedExitTime() = _state.value.exitTime
        .format(DateTimeFormatter.ofPattern("hh:mm a", Locale("es", "MX")))
}