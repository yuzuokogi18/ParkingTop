package com.parking.parkingtop.features.cliente.Reviews.data.datasources.mappers

import com.parking.parkingtop.features.cliente.Reviews.data.datasources.models.ReviewDTO
import com.parking.parkingtop.features.cliente.Reviews.domain.entities.Review

fun ReviewDTO.toDomain() = Review(
    id = id,
    parkingLotId = parkingLotId,
    reservationId = reservationId,
    rating = rating,
    comment = comment,
    ownerResponse = ownerResponse,
    createdAt = createdAt,
    parkingName = parkingLot?.name ?: ""
)