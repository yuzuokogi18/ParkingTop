package com.parking.parkingtop.features.cliente.CreateVehicleClient.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.cliente.CreateVehicleClient.domain.usecases.CreateVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateVehicleUiState(
    val isLoading: Boolean  = false,
    val isSuccess: Boolean  = false,
    val error: String?      = null
)

@HiltViewModel
class CreateVehicleViewModel @Inject constructor(
    private val useCase: CreateVehicleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateVehicleUiState())
    val state: StateFlow<CreateVehicleUiState> = _state

    fun createVehicle(
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        isDefault: Boolean
    ) {
        viewModelScope.launch {
            _state.value = CreateVehicleUiState(isLoading = true)

            val result = useCase.execute(licensePlate, brand, model, color, isDefault)

            result.onSuccess {
                _state.value = CreateVehicleUiState(isSuccess = true)
            }
            result.onFailure { e ->
                _state.value = CreateVehicleUiState(
                    error = when (e.message) {
                        "DUPLICATE_VEHICLE" -> "Ya tienes un vehículo con esas placas"
                        else                -> e.message ?: "Error al guardar el vehículo"
                    }
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}