package com.example.parkingtop.features.propetario.reservationpropetario.data.datasources.mapper

import com.example.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationOwnerDto
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus

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
