package com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateParkingDto(
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val latitude: Double,
    val longitude: Double,
    val totalSpots: Int,
    val basePricePerHour: Double,
    val overtimeRatePerHour: Double,
    val features: List<String>
)
