package com.example.parkingtop.features.propetario.homepropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class HomePropetarioResponse(
    val ownerName: String,
    val summary: SummaryDto,
    val notifications: List<NotificationDto>,
    val parkings: List<ParkingDto>
)

@Serializable
data class SummaryDto(
    val occupationPercentage: Int,
    val earnings: Double
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
    val occupiedSpaces: Int,
    val totalSpaces: Int,
    val reservationsCount: Int,
    val rating: Float
)
