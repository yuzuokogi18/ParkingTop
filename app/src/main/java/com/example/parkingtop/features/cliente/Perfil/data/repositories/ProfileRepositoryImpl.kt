package com.example.parkingtop.features.cliente.Perfil.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.Perfil.data.datasources.mapper.toDomain
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import com.example.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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
                // Limpiar tokens locales
                tokenDataStore.clearTokens()
                Result.success(Unit)
            } else {
                // Aunque falle en el servidor, limpiar tokens locales
                tokenDataStore.clearTokens()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            // En caso de error, igual limpiar tokens locales
            tokenDataStore.clearTokens()
            Result.success(Unit)
        }
    }

    override suspend fun deleteVehicle(vehicleId: String): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.first()
                ?: return Result.failure(Exception("No hay sesión activa"))

            val response = api.deleteVehicle(
                token     = "Bearer $token",
                vehicleId = vehicleId
            )

            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al eliminar el vehículo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}