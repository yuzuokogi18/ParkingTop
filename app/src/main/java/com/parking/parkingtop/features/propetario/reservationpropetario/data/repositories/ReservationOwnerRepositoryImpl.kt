package com.parking.parkingtop.features.propetario.reservationpropetario.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ReservationOwnerRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ReservationOwnerRepository {

    override suspend fun getReservations(status: String?): Result<List<ReservationOwner>> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val response = api.getOwnerReservations("Bearer $token", status)
            if (response.isSuccessful) {
                val data = response.body()?.data?.map { it.toDomain() } ?: emptyList()
                Result.success(data)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatus(id: String, status: ReservationStatus): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val statusString = status.name.lowercase()
            val response = api.updateReservationStatus("Bearer $token", id, mapOf("status" to statusString))
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
