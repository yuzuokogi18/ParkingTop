package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.usecases.GetParkingDetailUseCase
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens.ParkingDetalleUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingDetalleViewModel @Inject constructor(
    private val uc: GetParkingDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParkingDetalleUIState())
    val state: StateFlow<ParkingDetalleUIState> = _state


    fun loadDetail(parkingID: String) {

        viewModelScope.launch {

            _state.value = _state.value.copy(
                loading = true,
                error = null
            )

            val result = uc.execute(parkingID)

            result.onSuccess { parkingLot ->

                _state.value = _state.value.copy(
                    loading = false,
                    parking = parkingLot,
                    success = true
                )

            }

            result.onFailure { exception ->

                _state.value = _state.value.copy(
                    loading = false,
                    error = exception.message
                )

            }
        }
    }
}