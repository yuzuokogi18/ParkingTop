package com.parking.parkingtop.features.propetario.misespaciospropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.propetario.homepropetario.domain.usecases.GetHomeDataUseCase
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.SpotStatus
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.usecases.GetParkingSpotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MySpacesState(
    val isLoading: Boolean = false,
    val parkingLotId: String? = null,
    val parkingName: String = "",
    val spots: List<ParkingSpot> = emptyList(),
    val filteredSpots: List<ParkingSpot> = emptyList(),
    val selectedFilter: String = "Todos",
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class MySpacesViewModel @Inject constructor(
    private val getParkingSpotsUseCase: GetParkingSpotsUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _state = mutableStateOf(MySpacesState())
    val state: State<MySpacesState> = _state

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // Primero obtenemos el ID del estacionamiento del propietario desde el home data
            getHomeDataUseCase().fold(
                onSuccess = { homeData ->
                    val parkingId = homeData.parkings.firstOrNull()?.id
                    if (parkingId != null) {
                        _state.value = _state.value.copy(
                            parkingLotId = parkingId,
                            parkingName = homeData.parkings.firstOrNull()?.name ?: ""
                        )
                        loadSpots(parkingId)
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "No tienes estacionamientos registrados"
                        )
                    }
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar datos del propietario"
                    )
                }
            )
        }
    }

    fun loadSpots(parkingLotId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getParkingSpotsUseCase(parkingLotId).fold(
                onSuccess = { spots ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        spots = spots,
                        filteredSpots = spots
                    )
                    applyFilters()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar los espacios"
                    )
                }
            )
        }
    }

    fun onFilterSelected(filter: String) {
        _state.value = _state.value.copy(selectedFilter = filter)
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun applyFilters() {
        val currentSpots = _state.value.spots
        val filter = _state.value.selectedFilter
        val query = _state.value.searchQuery.lowercase()

        var filtered = currentSpots

        // Aplicar filtro por estado
        if (filter != "Todos") {
            val statusToFilter = when (filter) {
                "Disponibles" -> SpotStatus.AVAILABLE
                "Ocupados" -> SpotStatus.OCCUPIED
                "Reservados" -> SpotStatus.RESERVED
                "Mantenimiento" -> SpotStatus.MAINTENANCE
                else -> null
            }
            if (statusToFilter != null) {
                filtered = filtered.filter { it.status == statusToFilter }
            }
        }

        // Aplicar búsqueda por número de espacio
        if (query.isNotEmpty()) {
            filtered = filtered.filter { 
                it.spotNumber.lowercase().contains(query) || 
                (it.floor?.lowercase()?.contains(query) == true) ||
                (it.section?.lowercase()?.contains(query) == true)
            }
        }

        _state.value = _state.value.copy(filteredSpots = filtered)
    }
}
