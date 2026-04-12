package com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityResponseDto(
    val isPublished: Boolean,
    val calendarDays: List<CalendarDayDto>,
    val occupancyInfo: List<OccupancyInfoDto>
)

@Serializable
data class CalendarDayDto(
    val day: Int,
    val month: Int,
    val year: Int,
    val status: String,
    val occupiedSpaces: Int? = null,
    val totalSpaces: Int? = null
)

@Serializable
data class OccupancyInfoDto(
    val date: String,
    val occupied: Int,
    val total: Int,
    val percentage: Int
)
