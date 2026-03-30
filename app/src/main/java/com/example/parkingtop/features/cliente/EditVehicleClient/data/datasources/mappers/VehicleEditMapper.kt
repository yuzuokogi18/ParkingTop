package com.example.parkingtop.features.cliente.EditVehicleClient.data.datasources.mappers

import com.example.parkingtop.features.cliente.EditVehicleClient.domain.entities.VehicleDetail
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO

fun VehicleDTO.toVehicleDetail() = VehicleDetail(
    id           = id,
    licensePlate = licensePlate,
    brand        = brand,
    model        = model,
    color        = color,
    isDefault    = isDefault
)
