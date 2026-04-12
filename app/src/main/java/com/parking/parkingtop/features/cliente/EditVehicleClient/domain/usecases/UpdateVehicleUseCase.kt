package com.parking.parkingtop.features.cliente.EditVehicleClient.domain.usecases

import com.parking.parkingtop.features.cliente.EditVehicleClient.domain.entities.VehicleDetail
import com.parking.parkingtop.features.cliente.EditVehicleClient.domain.repositories.EditVehicleRepository
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(
    private val repository: EditVehicleRepository
) {
    suspend fun execute(
        vehicleId: String,
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        vehicleType: String,
        isDefault: Boolean
    ): Result<VehicleDetail> {
        if (licensePlate.isBlank()) {
            return Result.failure(Exception("La placa del vehículo es requerida"))
        }
        return repository.updateVehicle(vehicleId, licensePlate, brand, model, color, vehicleType, isDefault)
    }
}