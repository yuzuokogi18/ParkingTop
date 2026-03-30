package com.example.parkingtop.features.cliente.CreateVehicleClient.domain.entities

data class Vehicle(
    val id: String,
    val licensePlate: String,
    val brand: String?,
    val model: String?,
    val color: String?,
    val isDefault: Boolean
)