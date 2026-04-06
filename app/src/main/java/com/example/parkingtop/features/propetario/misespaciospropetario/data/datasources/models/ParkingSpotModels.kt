package com.example.parkingtop.features.propetario.misespaciospropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpotDto(
    val id: String,
    val parkingLotId: String,
    val spotNumber: String,
    val status: String,       // available | occupied | reserved | maintenance
    val vehicleType: String,  // car, etc.
    val floor: String? = null,
    val section: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class CreateParkingSpotRequest(
    val parkingLotId: String,
    val spotNumber: String,
    val status: String? = null,
    val vehicleType: String? = null,
    val floor: String? = null,
    val section: String? = null
)

@Serializable
data class UpdateParkingSpotRequest(
    val spotNumber: String? = null,
    val status: String? = null,
    val vehicleType: String? = null,
    val floor: String? = null,
    val section: String? = null
)

@Serializable
data class DeleteMessageDto(
    val message: String
)
