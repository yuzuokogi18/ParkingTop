package com.example.parkingtop.features.cliente.EditVehicleClient.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.cliente.EditVehicleClient.domain.usecases.UpdateVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditVehicleUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String?     = null
)

@HiltViewModel
class EditVehicleViewModel @Inject constructor(
    private val useCase: UpdateVehicleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditVehicleUiState())
    val state: StateFlow<EditVehicleUiState> = _state

    fun updateVehicle(
        vehicleId: String,
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        vehicleType: String,
        isDefault: Boolean
    ) {
        viewModelScope.launch {
            _state.value = EditVehicleUiState(isLoading = true)

            val result = useCase.execute(vehicleId, licensePlate, brand, model, color, vehicleType, isDefault)

            result.onSuccess {
                _state.value = EditVehicleUiState(isSuccess = true)
            }
            result.onFailure { e ->
                _state.value = EditVehicleUiState(error = e.message ?: "Error al actualizar")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}