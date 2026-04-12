package com.parking.parkingtop.features.cliente.HomeClient.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.HomeClient.domain.repositories.HomeRepository
import com.parking.parkingtop.features.cliente.HomeClient.data.datasources.mappper.toDomain
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : HomeRepository {

    override suspend fun getNearbyParkings(lat: Double, lng: Double): Result<List<ParkingLot>> {
        return try {
            val response = api.getNearbyParkings(lat, lng)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener estacionamientos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<User> {
        return try {
            val token = tokenDataStore.accessToken.first()
            if (token == null) return Result.failure(Exception("No hay sesión activa"))
            
            val response = api.getProfile("Bearer $token")
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
