package com.parking.parkingtop.features.cliente.ReservaClient.domain.entities

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val licensePlate: String,
    val isDefault: Boolean = false   // ✅ nuevo campo
)


data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val basePricePerHour: Double,
    val overtimeRatePerHour: Double
)

data class ReservationRequest(
    val parkingLotId: String,
    val vehicleId: String,
    val startTime: String,
    val endTime: String,
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
    val discounts: Double     = 0.0,
    val total: Double
)

data class ParkingSpot(
    val id: String,
    val spotNumber: String,
    val status: String,
    val vehicleType: String,
    val floor: String?,
    val section: String?,
    val isAvailable: Boolean
)

data class ReservationResult(
    val reservationId: String,
    val reservationCode: String,
    val paymentUrl: String?,     // URL de MercadoPago (null si es efectivo)
    val paymentMethod: String,
    val totalCost: Double,
    val isCash: Boolean,         // true = pagar al llegar
    val message: String?
)