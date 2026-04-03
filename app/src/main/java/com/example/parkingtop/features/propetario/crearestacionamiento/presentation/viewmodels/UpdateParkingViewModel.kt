package com.example.parkingtop.features.propetario.crearestacionamiento.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.OperatingHours
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class UpdateParkingState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val parkingData: CreateParkingData? = null,
    val error: String? = null
)

@HiltViewModel
class UpdateParkingViewModel @Inject constructor(
    private val repository: CreateParkingRepository
) : ViewModel() {

    private val _state = mutableStateOf(UpdateParkingState())
    val state: State<UpdateParkingState> = _state

    fun loadParking(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            repository.getParkingById(id).fold(
                onSuccess = { data ->
                    _state.value = _state.value.copy(isLoading = false, parkingData = data)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
            )
        }
    }

    fun updateParking(
        id: String,
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
            _state.value = _state.value.copy(isLoading = true, error = null)
            
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

            repository.updateParking(id, data).fold(
                onSuccess = {
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
            )
        }
    }
}
