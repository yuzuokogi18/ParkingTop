package com.example.parkingtop.features.propetario.horariopropetario.data.datasources.mapper

import com.example.parkingtop.features.propetario.horariopropetario.data.datasources.models.*
import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.*

fun AvailabilityResponseDto.toDomain(): AvailabilityData {
    return AvailabilityData(
        isPublished = isPublished,
        calendarDays = calendarDays.map { it.toDomain() },
        expectedOccupancy = occupancyInfo.map { it.toDomain() }
    )
}

fun CalendarDayDto.toDomain(): CalendarDay {
    return CalendarDay(
        day = day,
        month = month,
        year = year,
        status = when (status.lowercase()) {
            "free" -> DayStatus.FREE
            "partial" -> DayStatus.PARTIAL
            "full" -> DayStatus.FULL
            "blocked" -> DayStatus.BLOCKED
            else -> DayStatus.FREE
        },
        occupiedSpaces = occupiedSpaces,
        totalSpaces = totalSpaces
    )
}

fun OccupancyInfoDto.toDomain(): OccupancyInfo {
    return OccupancyInfo(
        date = date,
        occupiedSpaces = occupied,
        totalSpaces = total,
        percentage = percentage
    )
}
