package com.example.parkingtop.features.propetario.crearestacionamiento.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.usecases.CreateParkingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class CreateParkingState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateParkingViewModel @Inject constructor(
    private val createParkingUseCase: CreateParkingUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CreateParkingState())
    val state: State<CreateParkingState> = _state

    fun createParking(
        name: String,
        address: String,
        city: String,
        state: String,
        latitude: Double,
        longitude: Double,
        totalSpots: Int,
        basePricePerHour: Double,
        overtimeRatePerHour: Double,
        features: List<String>,
        images: List<File>
    ) {
        viewModelScope.launch {
            _state.value = CreateParkingState(isLoading = true)
            
            val data = CreateParkingData(
                name = name,
                address = address,
                city = city,
                state = state,
                latitude = latitude,
                longitude = longitude,
                totalSpots = totalSpots,
                basePricePerHour = basePricePerHour,
                overtimeRatePerHour = overtimeRatePerHour,
                features = features,
                images = images
            )

            createParkingUseCase(data).fold(
                onSuccess = {
                    _state.value = CreateParkingState(isSuccess = true)
                },
                onFailure = { error ->
                    _state.value = CreateParkingState(error = error.message ?: "Error al crear estacionamiento")
                }
            )
        }
    }
}
