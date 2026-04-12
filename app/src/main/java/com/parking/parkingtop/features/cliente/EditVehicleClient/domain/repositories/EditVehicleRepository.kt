package com.parking.parkingtop.features.cliente.EditVehicleClient.domain.repositories

import com.parking.parkingtop.features.cliente.EditVehicleClient.domain.entities.VehicleDetail

interface EditVehicleRepository {
    suspend fun updateVehicle(
        vehicleId: String,
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        vehicleType: String,
        isDefault: Boolean
    ): Result<VehicleDetail>
}
