package com.parking.parkingtop.features.propetario.horariopropetario.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.propetario.horariopropetario.domain.entities.AvailabilityData
import com.parking.parkingtop.features.propetario.horariopropetario.domain.repositories.AvailabilityRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AvailabilityRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : AvailabilityRepository {

    override suspend fun getAvailability(): Result<AvailabilityData> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val response = api.getAvailability("Bearer $token")
            if (response.isSuccessful) {
                val data = response.body()?.data?.toDomain()
                if (data != null) Result.success(data)
                else Result.failure(Exception("Cuerpo de respuesta vacío"))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePublishStatus(isPublished: Boolean): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val response = api.updatePublishStatus("Bearer $token", mapOf("status" to isPublished))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
