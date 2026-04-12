package com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities

data class ParkingLot(
    val id: String,
    val name: String,
    val description: String?,
    val address: String,
    val city: String,
    val state: String,
    val latitude: Double,
    val longitude: Double,
    val totalSpots: Int,
    val availableSpots: Int,
    val pricePerHour: Double,
    val features: List<String>,
    val images: List<String>,
    val rating: Double,
    val totalReviews: Int,
    val ownerName: String
)