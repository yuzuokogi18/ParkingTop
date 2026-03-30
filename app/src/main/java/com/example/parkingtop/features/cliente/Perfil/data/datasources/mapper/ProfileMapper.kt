package com.example.parkingtop.features.cliente.Perfil.data.datasources.mapper

import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.ReservationDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.UserDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        email = email,
        fullName = fullName,
        phone = phone,
        role = role,
        profileImageUrl = profileImageUrl,
        isActive = status == "active",
        emailVerified = emailVerified,
        createdAt = createdAt,
        updatedAt = lastLoginAt
    )
}

fun VehicleDTO.toDomain(): Vehicle {
    return Vehicle(
        id = this.id,
        brand = this.brand,
        model = this.model,
        year = this.year,
        color = this.color,
        licensePlate = this.licensePlate,
        isDefault = this.isDefault
    )
}

fun ReservationDTO.toDomain(): Reservation {
    return Reservation(
        id = this.id,
        parkingLotName = this.parkingLot.name,
        parkingLotAddress = this.parkingLot.address,
        startTime = this.startTime,
        endTime = this.endTime,
        totalCost = this.totalCost,
        status = this.status,
        reservationCode = this.reservationCode
    )
}