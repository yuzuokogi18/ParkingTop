package com.example.parkingtop.features.propetario.reservationpropetario.domain.entities

data class ReservationOwner(
    val id: String,
    val clientName: String,
    val vehicleModel: String,
    val licensePlate: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val status: ReservationStatus
)

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}
