package com.parking.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.usecases.GetParkingLotsToMarkersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val markers: List<ParkingMarker> = emptyList(),
    val selectedMarker: ParkingMarker? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ParkingMapViewModel @Inject constructor(
    private val uc: GetParkingLotsToMarkersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState(isLoading = true))
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadParkings()
    }

    fun loadParkings() {
        viewModelScope.launch {
            _uiState.value = MapUiState(isLoading = true)
            try {
                val lots = uc.execute()   // tu método existente

                lots.onSuccess { lots ->
                    _uiState.value = MapUiState(
                        markers = lots
                    )
                }
            } catch (e: Exception) {
                _uiState.value = MapUiState(error = e.message)
            }
        }
    }

    fun onMarkerSelected(marker: ParkingMarker) {
        _uiState.value = _uiState.value.copy(selectedMarker = marker)
    }

    fun onMarkerDismissed() {
        _uiState.value = _uiState.value.copy(selectedMarker = null)
    }
}