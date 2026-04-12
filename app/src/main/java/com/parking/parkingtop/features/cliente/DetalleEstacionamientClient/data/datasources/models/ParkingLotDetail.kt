package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models

import kotlinx.serialization.Serializable
import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.models.OperatingHoursDTO

@Serializable
data class ParkingLotDetailDTO(
    val id: String,
    val name: String,
    val description: String? = null,
    val address: String,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val images: List<String> = emptyList(),
    val availability: AvailabilityDTO,
    val pricing: PricingDTO,
    val operatingHours: Map<String, OperatingHoursDTO>? = null,
    val features: List<String> = emptyList(),
    val ratingAverage: String? = null,
    val totalReviews: Int = 0,
    val reviews: List<ReviewDTO> = emptyList()
)

@Serializable
data class AvailabilityDTO(
    val available: Int = 0,
    val total: Int = 0
)

@Serializable
data class PricingDTO(
    val basePricePerHour: String? = null,
    val overtimeRatePerHour: String? = null
)

@Serializable
data class ReviewDTO(
    val id: String,
    val rating: Int,
    val comment: String?,
    val user: UserReviewDTO
)

@Serializable
data class UserReviewDTO(
    val fullName: String,
    val profileImageUrl: String?
)
