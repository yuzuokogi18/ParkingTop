package com.parking.parkingtop.features.cliente.HomeClient.domain.entities

data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val availableSpots: Int,
    val basePricePerHour: String,
    val ratingAverage: String,
    val imageUrl: String? = null,
    val latitude: Double,
    val longitude: Double
)

data class UserProfile(
    val id: String,
    val fullName: String,
    val profileImageUrl: String? = null
)
