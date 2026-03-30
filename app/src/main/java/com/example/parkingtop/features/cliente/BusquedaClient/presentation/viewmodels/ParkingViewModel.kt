package com.example.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.BusquedaClient.domain.usecases.GetParkingLotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingViewModel @Inject constructor(
    private val getParkingLotsUseCase: GetParkingLotsUseCase
) : ViewModel() {

    private val _parkingLots = MutableStateFlow<List<ParkingLot>>(emptyList())
    val parkingLots: StateFlow<List<ParkingLot>> = _parkingLots

    init {
        loadParkingLots()
    }

    fun loadParkingLots() {
        viewModelScope.launch {

            val result = getParkingLotsUseCase()

            result.onSuccess {
                _parkingLots.value = it
            }

        }
    }
}