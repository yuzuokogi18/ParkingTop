package com.parking.parkingtop.features.cliente.EditVehicleClient.data.datasources.mappers

import com.parking.parkingtop.features.cliente.EditVehicleClient.domain.entities.VehicleDetail
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO

fun VehicleDTO.toVehicleDetail() = VehicleDetail(
    id = id,
    licensePlate = licensePlate,
    brand = brand,
    model = model,
    color = color,
    isDefault = isDefault
)
