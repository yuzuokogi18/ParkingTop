package com.example.parkingtop.features.propetario.reservationpropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class ReservationOwnerDto(
    val id: String,
    val clientName: String,
    val vehicleModel: String,
    val licensePlate: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val status: String
)
