package com.parking.parkingtop.features.cliente.CreateVehicleClient.domain.usecases

import com.parking.parkingtop.features.cliente.CreateVehicleClient.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.CreateVehicleClient.domain.repositories.CreateVehicleRepository
import javax.inject.Inject

class CreateVehicleUseCase @Inject constructor(
    private val repository: CreateVehicleRepository
) {
    suspend fun execute(
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        isDefault: Boolean
    ): Result<Vehicle> {
        if (licensePlate.isBlank()) {
            return Result.failure(Exception("La placa del vehículo es requerida"))
        }
        return repository.createVehicle(licensePlate, brand, model, color, isDefault)
    }
}