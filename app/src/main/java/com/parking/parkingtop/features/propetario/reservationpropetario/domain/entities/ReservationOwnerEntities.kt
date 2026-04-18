package com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities

data class ReservationOwner(
    val id: String,
    val clientName: String,
    val vehicleModel: String,
    val licensePlate: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val status: ReservationStatus,
    val paymentMethod: String? = null,   // "cash" | "card" | null
    val checkInTime: String? = null,
    val actualExitTime: String? = null
)

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    ACTIVE, // 🔥 o crea ACTIVE si quieres más fino
    CANCELLED,
    COMPLETED
}