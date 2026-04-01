package com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities

import java.io.File

data class CreateParkingData(
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val latitude: Double,
    val longitude: Double,
    val totalSpots: Int,
    val basePricePerHour: Double,
    val overtimeRatePerHour: Double,
    val features: List<String>,
    val images: List<File> = emptyList()
)
