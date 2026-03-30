package com.example.parkingtop.features.cliente.EditVehicleClient.domain.entities

data class VehicleDetail(
    val id: String,
    val licensePlate: String,
    val brand: String,
    val model: String,
    val color: String,
    val isDefault: Boolean
)