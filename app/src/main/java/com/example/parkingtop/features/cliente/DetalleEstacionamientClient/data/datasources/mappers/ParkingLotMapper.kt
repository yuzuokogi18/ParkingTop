package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.mappers

import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot

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
        ratingAverage = ratingAverage,
        reviews= reviews,
    )
}