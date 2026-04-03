package com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities

import java.io.File

data class CreateParkingData(
    val name: String,
    val description: String,
    val address: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val latitude: Double,
    val longitude: Double,
    val totalSpots: Int,
    val basePricePerHour: Double,
    val overtimeRatePerHour: Double,
    val features: List<String>,
    val operatingHours: OperatingHours,
    val images: List<File> = emptyList()
)

data class OperatingHours(
    val monday: DayHours,
    val tuesday: DayHours,
    val wednesday: DayHours,
    val thursday: DayHours,
    val friday: DayHours,
    val saturday: DayHours,
    val sunday: DayHours
)

data class DayHours(
    val open: String,
    val close: String
)
