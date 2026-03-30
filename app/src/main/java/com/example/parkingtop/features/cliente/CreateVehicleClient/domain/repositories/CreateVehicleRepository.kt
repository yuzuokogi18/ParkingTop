package com.example.parkingtop.features.cliente.CreateVehicleClient.domain.repositories

import com.example.parkingtop.features.cliente.CreateVehicleClient.domain.entities.Vehicle

interface CreateVehicleRepository {
    suspend fun createVehicle(
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        isDefault: Boolean
    ): Result<Vehicle>
}
