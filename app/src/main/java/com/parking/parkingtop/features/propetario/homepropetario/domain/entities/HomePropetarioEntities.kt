package com.parking.parkingtop.features.propetario.homepropetario.domain.entities

data class HomePropetarioData(
    val ownerName: String,
    val summary: GeneralSummary,
    val notifications: List<OwnerNotification>,
    val parkings: List<OwnerParking>
)

data class GeneralSummary(
    val currentOccupationPercentage: Int,
    val monthlyEarnings: Double
)

data class OwnerNotification(
    val id: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean
)

data class OwnerParking(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val occupiedSpaces: Int,
    val totalSpaces: Int,
    val nextReservationsCount: Int,
    val rating: Float
)
