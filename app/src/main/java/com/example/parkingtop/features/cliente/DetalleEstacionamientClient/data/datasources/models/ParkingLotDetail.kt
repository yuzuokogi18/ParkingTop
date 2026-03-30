package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models

import kotlinx.serialization.Serializable


    @Serializable
    data class ParkingLotDetailDTO(
        val id: String,
        val name: String,
        val description: String?,
        val address: String,
        val images: List<String>,
        val availability: AvailabilityDTO,
        val pricing: PricingDTO,
        val features: List<String>,
        val ratingAverage: Double,
        val reviews: List<ReviewDTO>
    )

    @Serializable
    data class AvailabilityDTO(
        val available: Int,
        val total: Int
    )

    @Serializable
    data class PricingDTO(
        val basePricePerHour: Double,
        val overtimeRatePerHour: Double
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