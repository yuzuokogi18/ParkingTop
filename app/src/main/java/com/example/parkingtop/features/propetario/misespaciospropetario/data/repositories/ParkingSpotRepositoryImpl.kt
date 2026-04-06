package com.example.parkingtop.features.propetario.misespaciospropetario.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.misespaciospropetario.data.datasources.mapper.toDomain
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.repositories.ParkingSpotRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ParkingSpotRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ParkingSpotRepository {

    private suspend fun getAuthToken(): String {
        val token = tokenDataStore.accessToken.firstOrNull() ?: ""
        return "Bearer $token"
    }

    override suspend fun getByParkingLotId(parkingLotId: String): Result<List<ParkingSpot>> {
        return try {
            val response = api.getByParkingLotId(getAuthToken(), parkingLotId)
            val apiResponse = response.body()
            
            if (response.isSuccessful && apiResponse != null && apiResponse.success) {
                Result.success(apiResponse.data?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception(apiResponse?.error?.message ?: "Error al obtener espacios"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(id: String): Result<ParkingSpot> {
        return try {
            val response = api.getParkingSpotById(getAuthToken(), id)
            val apiResponse = response.body()
            
            if (response.isSuccessful && apiResponse != null && apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data.toDomain())
            } else {
                Result.failure(Exception(apiResponse?.error?.message ?: "Error al obtener detalle del espacio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return try {
            val response = api.deleteParkingSpot(getAuthToken(), id)
            val apiResponse = response.body()
            
            if (response.isSuccessful && apiResponse != null && apiResponse.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(apiResponse?.error?.message ?: "Error al eliminar espacio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
