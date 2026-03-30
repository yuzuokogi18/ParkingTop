package com.example.parkingtop.features.cliente.EditVehicleClient.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.EditVehicleClient.data.datasources.mappers.toVehicleDetail
import com.example.parkingtop.features.cliente.EditVehicleClient.domain.entities.VehicleDetail
import com.example.parkingtop.features.cliente.EditVehicleClient.domain.repositories.EditVehicleRepository
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EditVehicleRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : EditVehicleRepository {

    override suspend fun updateVehicle(
        vehicleId: String,
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        vehicleType: String,
        isDefault: Boolean
    ): Result<VehicleDetail> {
        return try {
            val token = tokenDataStore.accessToken.first()
                ?: return Result.failure(Exception("No hay sesión activa"))

            val response = api.updateVehicle(
                token     = "Bearer $token",
                vehicleId = vehicleId,
                vehicle   = VehicleDTO(
                    id           = vehicleId,
                    licensePlate = licensePlate,
                    brand        = brand ?: "",
                    model        = model ?: "",
                    year         = 0,
                    color        = color ?: "",
                    isDefault    = isDefault
                )
            )

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toVehicleDetail())
            } else {
                val msg = when (response.body()?.error?.message) {
                    "VEHICLE_NOT_FOUND"  -> "Vehículo no encontrado"
                    "DUPLICATE_VEHICLE"  -> "Ya existe un vehículo con esas placas"
                    else                 -> "Error al actualizar el vehículo"
                }
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}