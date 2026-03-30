package com.example.parkingtop.features.cliente.CreateVehicleClient.data.datasources.mapper

import com.example.parkingtop.features.cliente.CreateVehicleClient.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO

fun VehicleDTO.toDomain() = Vehicle(
    id           = id,
    licensePlate = licensePlate,
    brand        = brand,
    model        = model,
    color        = color,
    isDefault    = isDefault
)
