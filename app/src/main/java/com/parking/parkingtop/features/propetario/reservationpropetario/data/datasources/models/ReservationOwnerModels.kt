package com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class ReservationOwnerDto(
    val id: String,
    val clientName: String,
    val vehicleModel: String,
    val licensePlate: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val status: String
)

@Serializable
data class ConfirmCashPaymentDto(
    val message: String,
    val reservation: ReservationDto
)

@Serializable
data class ReservationDto(
    val id: String,
    val reservationCode: String,
    val status: String,
    val paymentMethod: String? = null,
    val startTime: String,
    val endTime: String,
    val totalCost: String? = null,
    val parkingLot: ParkingLotDto? = null,
    val parkingSpot: ParkingSpotDto? = null,
    val user: UserDto? = null
)

@Serializable
data class ParkingLotDto(
    val id: String,
    val name: String,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null
)

@Serializable
data class ParkingSpotDto(
    val id: String,
    val spotNumber: String? = null
)

@Serializable
data class UserDto(
    val id: String,
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null
)


@Serializable
data class CheckInCheckOutResponse(
    val message: String,
    val reservation: Reservation
)

@Serializable
data class Reservation(
    val id: String,
    val reservationCode: String,
    val userId: String,
    val parkingLotId: String,
    val parkingSpotId: String?,
    val vehicleId: String?,

    val startTime: String,
    val endTime: String,
    val actualExitTime: String?,
    val checkInTime: String?,

    val reservedHours: String,
    val baseCost: String,
    val overtimeCost: String,
    val totalCost: String,

    val status: String, // pending | confirmed | active | completed | cancelled | no_show
    val paymentMethod: String, // mercadopago | cash

    val createdAt: String,
    val updatedAt: String
)