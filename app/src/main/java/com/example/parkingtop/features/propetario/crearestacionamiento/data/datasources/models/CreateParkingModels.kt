package com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateParkingDto(
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
    val operatingHours: OperatingHoursDto
)

@Serializable
data class OperatingHoursDto(
    val monday: DayHoursDto,
    val tuesday: DayHoursDto,
    val wednesday: DayHoursDto,
    val thursday: DayHoursDto,
    val friday: DayHoursDto,
    val saturday: DayHoursDto,
    val sunday: DayHoursDto
)

@Serializable
data class DayHoursDto(
    val open: String,
    val close: String
)
