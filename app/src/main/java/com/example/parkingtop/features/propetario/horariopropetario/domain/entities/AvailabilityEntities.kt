package com.example.parkingtop.features.propetario.horariopropetario.domain.entities

data class AvailabilityData(
    val isPublished: Boolean,
    val calendarDays: List<CalendarDay>,
    val expectedOccupancy: List<OccupancyInfo>
)

data class CalendarDay(
    val day: Int,
    val month: Int,
    val year: Int,
    val status: DayStatus,
    val occupiedSpaces: Int? = null,
    val totalSpaces: Int? = null
)

enum class DayStatus {
    FREE,
    PARTIAL,
    FULL,
    BLOCKED
}

data class OccupancyInfo(
    val date: String,
    val occupiedSpaces: Int,
    val totalSpaces: Int,
    val percentage: Int
)
