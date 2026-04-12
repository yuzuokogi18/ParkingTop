package com.parking.parkingtop.features.cliente.Perfil.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.core.network.model.OwnerBalanceDto
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import com.parking.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ProfileRepository {

    private suspend fun getAuthToken(): String {
        val token = tokenDataStore.accessToken.first() ?: ""
        return "Bearer $token"
    }

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = api.getProfile(getAuthToken())

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(fullName: String, phone: String?): Result<User> {
        return try {
            val request = UpdateProfileRequest(fullName, phone)
            val response = api.updateProfile(getAuthToken(), request)

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al actualizar perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVehicles(): Result<List<Vehicle>> {
        return try {
            val response = api.getVehicles(getAuthToken())

            if (response.isSuccessful && response.body()?.data != null) {
                val vehicles = response.body()!!.data!!.map { it.toDomain() }
                Result.success(vehicles )
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener vehículos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReservations(status: String?): Result<List<Reservation>> {
        return try {
            val response = api.getReservations(getAuthToken(), status)

            if (response.isSuccessful && response.body()?.data != null) {
                val reservations = response.body()!!.data!!.map { it.toDomain() }
                Result.success(reservations)
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener reservas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = api.logout(getAuthToken())

            if (response.isSuccessful) {
                tokenDataStore.clearTokens()
                Result.success(Unit)
            } else {
                tokenDataStore.clearTokens()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            tokenDataStore.clearTokens()
            Result.success(Unit)
        }
    }

    override suspend fun deleteVehicle(vehicleId: String): Result<Unit> {
        return try {
            val response = api.deleteVehicle(getAuthToken(), vehicleId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar el vehículo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOwnerBalance(): Result<OwnerBalanceDto> {
        return try {
            val response = api.getBalance(getAuthToken())
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al obtener balance"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}