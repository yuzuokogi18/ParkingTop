package com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class HomePropetarioResponse(
    val ownerName: String,
    val summary: SummaryDto,
    val notifications: List<NotificationDto> = emptyList(),
    val parkings: List<ParkingDto> = emptyList()
)

@Serializable
data class SummaryDto(
    val occupationPercentage: Int = 0,
    val earnings: Double = 0.0
)

@Serializable
data class NotificationDto(
    val id: String,
    val message: String,
    val createdAt: String,
    val isRead: Boolean
)

@Serializable
data class ParkingDto(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val occupiedSpaces: Int = 0,
    val totalSpaces: Int = 0,
    val reservationsCount: Int = 0,
    val rating: Float = 0f
)
