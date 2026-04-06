package com.example.parkingtop.features.propetario.misespaciospropetario.data.datasources.mapper

import com.example.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.ParkingSpotDto
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.SpotStatus

fun ParkingSpotDto.toDomain(): ParkingSpot {
    return ParkingSpot(
        id = id,
        parkingLotId = parkingLotId,
        spotNumber = spotNumber,
        status = SpotStatus.fromString(status),
        vehicleType = vehicleType,
        floor = floor,
        section = section,
        updatedAt = updatedAt
    )
}
