package com.parking.parkingtop.features.propetario.crearestacionamiento.data.datasources.mapper

import com.parking.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.CreateParkingDto
import com.parking.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.DayHoursDto
import com.parking.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.OperatingHoursDto
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.DayHours
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.OperatingHours

fun CreateParkingData.toDto(): CreateParkingDto {
    return CreateParkingDto(
        name = name,
        description = description,
        address = address,
        city = city,
        state = state,
        postalCode = postalCode,
        latitude = latitude,
        longitude = longitude,
        totalSpots = totalSpots,
        basePricePerHour = basePricePerHour,
        overtimeRatePerHour = overtimeRatePerHour,
        features = features,
        operatingHours = operatingHours.toDto()
    )
}

fun OperatingHours.toDto(): OperatingHoursDto {
    return OperatingHoursDto(
        monday = monday.toDto(),
        tuesday = tuesday.toDto(),
        wednesday = wednesday.toDto(),
        thursday = thursday.toDto(),
        friday = friday.toDto(),
        saturday = saturday.toDto(),
        sunday = sunday.toDto()
    )
}

fun DayHours.toDto(): DayHoursDto {
    return DayHoursDto(open = open, close = close)
}
