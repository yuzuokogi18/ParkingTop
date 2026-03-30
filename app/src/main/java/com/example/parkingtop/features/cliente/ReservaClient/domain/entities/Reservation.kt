package com.example.parkingtop.features.cliente.ReservaClient.domain.entities
data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val basePricePerHour: Double,
    val overtimeRatePerHour: Double
)

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val licensePlate: String
)

data class ReservationRequest(
    val parkingLotId: String,
    val vehicleId: String,
    val startTime: String, // ISO 8601 format
    val endTime: String,   // ISO 8601 format
    val notes: String? = null
)

data class ReservationResponse(
    val reservationId: String,
    val paymentUrl: String,
    val totalCost: Double
)

data class PriceCalculation(
    val hours: Int,
    val baseCost: Double,
    val additionalTime: Double = 0.0,
    val discounts: Double = 0.0,
    val total: Double
)