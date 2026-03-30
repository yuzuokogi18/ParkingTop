package com.example.parkingtop.features.cliente.BusquedaClient.domain.entities

data class ParkingMarker(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val pricePerHour: Double,
    val availableSpots: Int,
    val rating: Double,
    val imageUrl: String?
)