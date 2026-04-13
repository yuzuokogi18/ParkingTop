package com.parking.parkingtop.features.propetario.crearestacionamiento.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.hardware.data.GPSManager
import com.parking.parkingtop.core.hardware.domain.entities.LocationData
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.OperatingHours
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.usecases.CreateParkingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class CreateParkingState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val location: LocationData? = null
)

@HiltViewModel
class CreateParkingViewModel @Inject constructor(
    private val createParkingUseCase: CreateParkingUseCase,
    private val gpsManager: GPSManager
) : ViewModel() {

    private val _state = mutableStateOf(CreateParkingState())
    val state: State<CreateParkingState> = _state

    fun getCurrentLocation() {
        viewModelScope.launch {
            if (!gpsManager.isGPSEnabled()) {
                _state.value = _state.value.copy(error = "El GPS está desactivado")
                return@launch
            }

            val location = gpsManager.getLastLocation()

            if (location != null) {
                _state.value = _state.value.copy(location = location, error = null)
            } else {
                _state.value = _state.value.copy(error = "No se pudo obtener la ubicación")
            }
        }
    }
    fun createParking(
        name: String,
        description: String,
        address: String,
        city: String,
        state: String,
        postalCode: String,
        latitude: Double,
        longitude: Double,
        totalSpots: Int,
        basePricePerHour: Double,
        overtimeRatePerHour: Double,
        features: List<String>,
        operatingHours: OperatingHours,
        images: List<File>
    ) {
        viewModelScope.launch {
            _state.value = CreateParkingState(isLoading = true)

            val data = CreateParkingData(
                name = name,
                description = description,
                address = address,
                city = city,
                state = state,
                postalCode = postalCode,
                latitude = latitude,
                longitude = longitude,
                totalSpots = totalSpots,
                basePricePerHour = basePricePerHour,
                overtimeRatePerHour = overtimeRatePerHour,
                features = features,
                operatingHours = operatingHours,
                images = images
            )

            createParkingUseCase(data).fold(
                onSuccess = {
                    _state.value = CreateParkingState(isSuccess = true)
                },
                onFailure = { error ->
                    _state.value = CreateParkingState(
                        error = error.message ?: "Error al crear estacionamiento"
                    )
                }
            )
        }
    }
}