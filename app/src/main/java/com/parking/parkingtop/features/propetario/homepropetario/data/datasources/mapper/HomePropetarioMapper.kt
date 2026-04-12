package com.parking.parkingtop.features.propetario.homepropetario.data.datasources.mapper

import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.HomePropetarioResponse
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.NotificationDto
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.ParkingDto
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.SummaryDto
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.GeneralSummary
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.OwnerNotification
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.OwnerParking

fun HomePropetarioResponse.toDomain(): HomePropetarioData {
    return HomePropetarioData(
        ownerName = ownerName,
        summary = summary.toDomain(),
        notifications = notifications.map { it.toDomain() },
        parkings = parkings.map { it.toDomain() }
    )
}

fun SummaryDto.toDomain(): GeneralSummary {
    return GeneralSummary(
        currentOccupationPercentage = occupationPercentage,
        monthlyEarnings = earnings
    )
}

fun NotificationDto.toDomain(): OwnerNotification {
    return OwnerNotification(
        id = id,
        message = message,
        timeAgo = createdAt, // Simplificación, idealmente formatear fecha
        isRead = isRead
    )
}

fun ParkingDto.toDomain(): OwnerParking {
    return OwnerParking(
        id = id,
        name = name,
        imageUrl = imageUrl,
        occupiedSpaces = occupiedSpaces,
        totalSpaces = totalSpaces,
        nextReservationsCount = reservationsCount,
        rating = rating
    )
}
