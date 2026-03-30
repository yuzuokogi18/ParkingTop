package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities

import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.AvailabilityDTO
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.PricingDTO
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ReviewDTO

data class ParkingLot(
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