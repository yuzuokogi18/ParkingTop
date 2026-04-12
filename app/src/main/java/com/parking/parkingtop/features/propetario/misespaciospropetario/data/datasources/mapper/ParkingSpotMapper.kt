package com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.mapper

import com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.ParkingSpotDto
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.SpotStatus

fun ParkingSpotDto.toDomain(): ParkingSpot {
    return ParkingSpot(
        id = id,
        parkingLotId = parkingLotId,
        spotNumber = spotNumber,
        status = SpotStatus.Companion.fromString(status),
        vehicleType = vehicleType,
        floor = floor,
        section = section,
        updatedAt = updatedAt
    )
}
