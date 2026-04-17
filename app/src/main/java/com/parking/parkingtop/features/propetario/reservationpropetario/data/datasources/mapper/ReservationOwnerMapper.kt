package com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.mapper

import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.models.CheckInCheckOutResponse
import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationOwnerDto
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationDto

fun ReservationOwnerDto.toDomain(): ReservationOwner {
    return ReservationOwner(
        id = id,
        clientName = clientName,
        vehicleModel = vehicleModel,
        licensePlate = licensePlate,
        startTime = startTime,
        endTime = endTime,
        price = price,
        status = when (status.lowercase()) {
            "pending" -> ReservationStatus.PENDING
            "confirmed" -> ReservationStatus.CONFIRMED
            "cancelled" -> ReservationStatus.CANCELLED
            "completed" -> ReservationStatus.COMPLETED
            else -> ReservationStatus.PENDING
        }
    )
}


fun ReservationDto.toDomain(): ReservationOwner {
    return ReservationOwner(
        id            = id,
        clientName    = user?.fullName ?: "Sin nombre",
        vehicleModel  = parkingSpot?.spotNumber ?: "Sin espacio",
        licensePlate  = reservationCode,
        startTime     = startTime,
        endTime       = endTime,
        price         = totalCost?.toDoubleOrNull() ?: 0.0,
        paymentMethod = paymentMethod,
        status        = when (status.lowercase()) {
            "pending"   -> ReservationStatus.PENDING
            "confirmed" -> ReservationStatus.CONFIRMED
            "active"    -> ReservationStatus.CONFIRMED // 🔥 o crea ACTIVE si quieres más fino
            "cancelled" -> ReservationStatus.CANCELLED
            "completed" -> ReservationStatus.COMPLETED
            else        -> ReservationStatus.PENDING
        }
    )
}


fun CheckInCheckOutResponse.toDomain(): ReservationOwner {
    return ReservationOwner(
        id = reservation.id,
        clientName = "Cliente", // ⚠️ opcional mejorar si backend envía user
        vehicleModel = reservation.parkingSpotId ?: "Sin espacio",
        licensePlate = reservation.reservationCode,
        startTime = reservation.startTime,
        endTime = reservation.endTime,
        price = reservation.totalCost.toDoubleOrNull() ?: 0.0,
        paymentMethod = reservation.paymentMethod,

        checkInTime = reservation.checkInTime,
        actualExitTime = reservation.actualExitTime,

        status = when (reservation.status.lowercase()) {
            "pending"   -> ReservationStatus.PENDING
            "confirmed" -> ReservationStatus.CONFIRMED
            "completed" -> ReservationStatus.COMPLETED
            "active"    -> ReservationStatus.CONFIRMED // 🔥 o crea ACTIVE si quieres más fino"
            "cancelled" -> ReservationStatus.CANCELLED
            else        -> ReservationStatus.PENDING
        }
    )
}