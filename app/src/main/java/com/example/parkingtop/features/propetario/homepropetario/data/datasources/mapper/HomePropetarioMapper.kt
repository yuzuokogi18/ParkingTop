package com.example.parkingtop.features.propetario.homepropetario.data.datasources.mapper

import com.example.parkingtop.features.propetario.homepropetario.data.datasources.models.*
import com.example.parkingtop.features.propetario.homepropetario.domain.entities.*

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
