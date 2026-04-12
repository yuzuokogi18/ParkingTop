package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.mappers

import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot

fun ParkingLotDetailDTO.toDomain(): ParkingLot {
    return ParkingLot(
        id = id,
        name = name,
        description = description,
        address = address,
        images = images,
        availability = availability,
        pricing = pricing,
        features = features,
        ratingAverage = ratingAverage?.toDoubleOrNull() ?: 0.0,
        reviews = reviews,
    )
}