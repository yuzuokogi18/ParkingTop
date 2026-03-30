package com.example.parkingtop.features.cliente.Perfil.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDTO(

    @SerialName("id")
    val id: String,

    @SerialName("email")
    val email: String,

    @SerialName("fullName")
    val fullName: String,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("role")
    val role: String,

    @SerialName("status")
    val status: String,

    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,

    @SerialName("emailVerified")
    val emailVerified: Boolean = false,

    @SerialName("phoneVerified")
    val phoneVerified: Boolean = false,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("lastLoginAt")
    val lastLoginAt: String? = null
)

@Serializable
data class VehicleDTO(
    val id: String,

    val brand: String,

    val model: String,

    val year: Int = 0,

    val color: String,

    val licensePlate: String,

    val isDefault: Boolean
)

data class ReservationDTO(
    val id: String,

    val reservationCode: String,

    val parkingLot: ParkingLotSummaryDTO,

    val startTime: String,

    val endTime: String,

    val totalCost: Double,

    val status: String
)

@Serializable
data class ParkingLotSummaryDTO(
    val id: String,

    val name: String,

    val address: String
)