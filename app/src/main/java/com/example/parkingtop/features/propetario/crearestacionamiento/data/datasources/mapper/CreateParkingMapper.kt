package com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.mapper

import com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.CreateParkingDto
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData

fun CreateParkingData.toDto(): CreateParkingDto {
    return CreateParkingDto(
        name = name,
        address = address,
        city = city,
        state = state,
        latitude = latitude,
        longitude = longitude,
        totalSpots = totalSpots,
        basePricePerHour = basePricePerHour,
        overtimeRatePerHour = overtimeRatePerHour,
        features = features
    )
}
