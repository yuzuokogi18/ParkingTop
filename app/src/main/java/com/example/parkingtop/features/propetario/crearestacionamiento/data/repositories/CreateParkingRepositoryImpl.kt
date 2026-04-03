package com.example.parkingtop.features.propetario.crearestacionamiento.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.OperatingHoursDTO
import com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.mapper.toDto
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.DayHours
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.OperatingHours
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CreateParkingRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : CreateParkingRepository {

    override suspend fun createParking(data: CreateParkingData): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            if (token.isEmpty()) return Result.failure(Exception("No hay sesión activa"))

            val response = api.createParking("Bearer $token", data.toDto())

            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.errorBody()?.string() ?: "Error al crear"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getParkingById(id: String): Result<CreateParkingData> {
        return try {
            val response = api.getParkingById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                val dto = response.body()!!.data!!

                val oh = dto.operatingHours
                val operatingHours = OperatingHours(
                    monday    = mapDay(oh?.get("monday")),
                    tuesday   = mapDay(oh?.get("tuesday")),
                    wednesday = mapDay(oh?.get("wednesday")),
                    thursday  = mapDay(oh?.get("thursday")),
                    friday    = mapDay(oh?.get("friday")),
                    saturday  = mapDay(oh?.get("saturday")),
                    sunday    = mapDay(oh?.get("sunday"))
                )

                Result.success(CreateParkingData(
                    name = dto.name,
                    description = dto.description ?: "",
                    address = dto.address,
                    city = dto.city ?: "",
                    state = dto.state ?: "",
                    postalCode = dto.postalCode ?: "",
                    latitude = dto.latitude?.toDoubleOrNull() ?: 0.0,
                    longitude = dto.longitude?.toDoubleOrNull() ?: 0.0,
                    totalSpots = dto.availability.total,
                    basePricePerHour = dto.pricing.basePricePerHour?.toDoubleOrNull() ?: 0.0,
                    overtimeRatePerHour = dto.pricing.overtimeRatePerHour?.toDoubleOrNull() ?: 0.0,
                    features = dto.features,
                    operatingHours = operatingHours
                ))
            } else {
                Result.failure(Exception("Error al obtener datos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapDay(dto: OperatingHoursDTO?): DayHours {
        return DayHours(dto?.open ?: "08:00", dto?.close ?: "20:00")
    }

    override suspend fun updateParking(id: String, data: CreateParkingData): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val response = api.updateParking("Bearer $token", id, data.toDto())
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.errorBody()?.string() ?: "Error al actualizar"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteParking(id: String): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            if (token.isEmpty()) return Result.failure(Exception("No hay sesión activa"))
            
            val response = api.deleteParking("Bearer $token", id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar el estacionamiento"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
