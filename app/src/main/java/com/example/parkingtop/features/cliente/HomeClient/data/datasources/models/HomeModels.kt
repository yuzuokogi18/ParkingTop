package com.example.parkingtop.features.cliente.HomeClient.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class ParkingLotDTO(
    val id: String,
    val ownerId: String,
    val name: String,
    val description: String? = null,
    val address: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val latitude: String,
    val longitude: String,
    val totalSpots: Int,
    val availableSpots: Int,
    val status: String,
    val basePricePerHour: String,
    val overtimeRatePerHour: String,
    val operatingHours: Map<String, OperatingHoursDTO>,
    val features: List<String>,
    val images: List<String>,
    val ratingAverage: String,
    val totalReviews: Int,
    val distance: Int? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class OperatingHoursDTO(
    val open: String,
    val close: String
)

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String? = null,
    val role: String,
    val profileImage: String? = null,
    val isActive: Boolean,
    val emailVerified: Boolean,
    val createdAt: String,
    val updatedAt: String
)
