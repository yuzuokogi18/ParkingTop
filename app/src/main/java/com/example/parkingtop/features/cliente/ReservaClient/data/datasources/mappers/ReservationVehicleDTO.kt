package com.example.parkingtop.features.cliente.ReservaClient.data.datasources.mappers

import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationResponseDTO
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.ParkingSpotDTO
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.ReservationVehicleDTO
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ReservationResult
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle

fun ReservationVehicleDTO.toDomain() = Vehicle(
    id           = id,
    brand        = brand ?: "",
    model        = model ?: "",
    licensePlate = licensePlate,
    isDefault    = isDefault
)

fun ParkingSpotDTO.toDomain() = ParkingSpot(
    id          = id,
    spotNumber  = spotNumber,
    status      = status,
    vehicleType = vehicleType,
    floor       = floor,
    section     = section,
    isAvailable = status == "available"
)

fun CreateReservationResponseDTO.toDomain() = ReservationResult(
    reservationId   = reservation.id,
    reservationCode = reservation.reservationCode,
    paymentUrl      = paymentUrl,
    paymentMethod   = paymentMethod,
    totalCost       = reservation.totalCost.toDoubleOrNull() ?: 0.0,
    isCash          = paymentMethod == "cash",
    message         = message
)