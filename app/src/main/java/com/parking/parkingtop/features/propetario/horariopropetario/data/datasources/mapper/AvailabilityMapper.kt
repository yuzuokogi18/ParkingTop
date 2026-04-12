package com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.mapper

import com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.models.AvailabilityResponseDto
import com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.models.CalendarDayDto
import com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.models.OccupancyInfoDto
import com.parking.parkingtop.features.propetario.horariopropetario.domain.entities.AvailabilityData
import com.parking.parkingtop.features.propetario.horariopropetario.domain.entities.CalendarDay
import com.parking.parkingtop.features.propetario.horariopropetario.domain.entities.DayStatus
import com.parking.parkingtop.features.propetario.horariopropetario.domain.entities.OccupancyInfo

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
