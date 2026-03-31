package com.example.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// ─── Estado de filtros ────────────────────────────────────────────────────────
data class FilterState(
    val maxPricePerHour: Float? = null,   // null = sin límite de precio
    val onlyAvailable: Boolean  = false,  // solo con espacios disponibles
    val minRating: Float        = 0f,     // 0 = sin filtro de rating
    val sortByDistance: Boolean = false   // ordenar por GPS
)

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val repository: ParkingRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Lista cruda del backend — nunca se modifica directamente
    private val _allParkingLots = MutableStateFlow<List<ParkingLot>>(emptyList())

    // Filtros activos
    private val _filters = MutableStateFlow(FilterState())
    val filters: StateFlow<FilterState> = _filters.asStateFlow()

    // Ubicación del usuario (null si aún no se obtuvo o sin permiso)
    private val _userLocation = MutableStateFlow<Location?>(null)

    // Lista final expuesta a la UI = cruda + filtros aplicados
    private val _parkingLots = MutableStateFlow<List<ParkingLot>>(emptyList())
    val parkingLots: StateFlow<List<ParkingLot>> = _parkingLots.asStateFlow()

    init {
        // Reactivamente aplica filtros cuando cambia cualquiera de las tres fuentes
        viewModelScope.launch {
            combine(_allParkingLots, _filters, _userLocation) { all, f, location ->
                applyFilters(all, f, location)
            }.collect { filtered ->
                _parkingLots.value = filtered
            }
        }
        loadParkingLots()
    }

    fun loadParkingLots() {
        viewModelScope.launch {
            try {
                val lots = repository.getParkingLots()
                lots.onSuccess {
                    pakings ->
                    _allParkingLots.value = pakings
                }
            } catch (_: Exception) { }
        }
    }

    // ── Actualizar filtros desde la UI ────────────────────────────────────────
    fun updateFilters(newFilters: FilterState) {
        _filters.value = newFilters
        if (newFilters.sortByDistance) fetchUserLocation()
    }

    fun clearFilters() {
        _filters.value = FilterState()
    }

    // ── Obtener última ubicación conocida del usuario ─────────────────────────
    @SuppressLint("MissingPermission")
    private fun fetchUserLocation() {
        viewModelScope.launch {
            try {
                val location = LocationServices
                    .getFusedLocationProviderClient(context)
                    .lastLocation
                    .await()
                _userLocation.value = location
            } catch (_: Exception) {
                _userLocation.value = null
            }
        }
    }

    // ── Lógica de filtrado y ordenamiento (puro, testeable) ───────────────────
    private fun applyFilters(
        lots: List<ParkingLot>,
        f: FilterState,
        userLocation: Location?
    ): List<ParkingLot> {
        var result = lots

        if (f.onlyAvailable) {
            result = result.filter { it.availableSpots > 0 }
        }

        if (f.maxPricePerHour != null) {
            result = result.filter { it.pricePerHour <= f.maxPricePerHour }
        }

        if (f.minRating > 0f) {
            result = result.filter { it.rating >= f.minRating }
        }

        if (f.sortByDistance && userLocation != null) {
            result = result.sortedBy { lot ->
                val lotLoc = Location("").apply {
                    latitude  = lot.latitude
                    longitude = lot.longitude
                }
                userLocation.distanceTo(lotLoc)
            }
        }

        return result
    }
}