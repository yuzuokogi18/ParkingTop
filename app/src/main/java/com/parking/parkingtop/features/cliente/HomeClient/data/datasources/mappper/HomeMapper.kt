package com.parking.parkingtop.features.cliente.HomeClient.data.datasources.mappper

import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingDTO
import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingLotDTO
import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.models.UserDTO
import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.models.UserProfileDTO
import com.parking.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.HomeClient.domain.entities.UserProfile

fun ParkingLotDTO.toDomain(): ParkingLot {
    return ParkingLot(
        id = this.id,
        name = this.name,
        address = this.address,
        availableSpots = this.availableSpots,
        basePricePerHour = this.basePricePerHour,
        ratingAverage = this.ratingAverage,
        imageUrl = this.images.firstOrNull(),
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0
    )
}

fun UserDTO.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        fullName = this.fullName,
        profileImageUrl = this.profileImage
    )
}

fun ParkingDTO.toDomain(): ParkingLot {
    return ParkingLot(
        id = this.id,
        name = this.name,
        address = this.address,
        availableSpots = this.availableSpots,
        basePricePerHour = this.basePricePerHour.toString(),
        ratingAverage = this.ratingAverage.toString(),
        imageUrl = this.images.firstOrNull(),
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun UserProfileDTO.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        fullName = this.fullName,
        profileImageUrl = this.profileImageUrl
    )
}