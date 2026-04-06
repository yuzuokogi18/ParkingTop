package com.example.parkingtop.features.cliente.ReservaClient.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.mappers.toDomain
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ReservationResult
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ReservationRepository {

    private suspend fun token() = "Bearer ${
        tokenDataStore.accessToken.first() ?: throw Exception("Sin sesión")
    }"

    override suspend fun getVehicles(): Result<List<Vehicle>> = try {
        val response = api.getVehiclesClient(token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.map { it.toDomain() })
        else Result.failure(Exception("Error al obtener vehículos"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getDefaultVehicle(): Result<Vehicle?> = try {
        val response = api.getDefaultVehicle(token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.toDomain())
        else Result.success(null)   // sin vehículo default no es error
    } catch (e: Exception) { Result.failure(e) }

    // ✅ Obtener espacios del estacionamiento
    override suspend fun getParkingSpots(parkingLotId: String): Result<List<ParkingSpot>> = try {
        val response = api.getParkingSpots(parkingLotId, token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.map { it.toDomain() })
        else Result.failure(Exception("Error al obtener espacios"))
    } catch (e: Exception) { Result.failure(e) }

    // ✅ Crear reserva real
    override suspend fun createReservation(request: CreateReservationRequest): Result<ReservationResult> = try {
        val response = api.createReservation(token(), request)
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.toDomain())
        else Result.failure(Exception(response.body()?.error?.message ?: "Error al crear reserva"))
    } catch (e: Exception) { Result.failure(e) }
}
