package com.example.parkingtop.features.cliente.CreateVehicleClient.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.CreateVehicleClient.data.datasources.mapper.toDomain
import com.example.parkingtop.features.cliente.CreateVehicleClient.data.datasources.models.CreateVehicleRequest
import com.example.parkingtop.features.cliente.CreateVehicleClient.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.CreateVehicleClient.domain.repositories.CreateVehicleRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateVehicleRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : CreateVehicleRepository {

    override suspend fun createVehicle(
        licensePlate: String,
        brand: String?,
        model: String?,
        color: String?,
        isDefault: Boolean
    ): Result<Vehicle> {
        return try {
            // ✅ accessToken en lugar de getToken()
            val token = tokenDataStore.accessToken.first()
                ?: return Result.failure(Exception("No hay sesión activa"))

            val response = api.createVehicle(
                token   = "Bearer $token",
                vehicle = CreateVehicleRequest(
                    licensePlate = licensePlate,
                    brand        = brand ?: "",
                    model        = model ?: "",
                    color        = color ?: "",
                    isDefault    = isDefault
                )
            )

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                val msg = response.body()?.error?.message ?: "Error al crear el vehículo"
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}