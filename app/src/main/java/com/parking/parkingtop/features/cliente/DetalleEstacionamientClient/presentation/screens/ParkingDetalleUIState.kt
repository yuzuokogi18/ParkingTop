package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.presentation.screens

import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot

data class ParkingDetalleUIState(
    val loading: Boolean = false,
    val error: String? = null,
    val parking: ParkingLot? = null,
    val success: Boolean = false
)