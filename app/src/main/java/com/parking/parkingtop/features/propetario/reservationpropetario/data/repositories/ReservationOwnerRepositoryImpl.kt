package com.parking.parkingtop.features.propetario.reservationpropetario.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ReservationOwnerRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ReservationOwnerRepository {

    private suspend fun bearerToken(): String =
        "Bearer ${tokenDataStore.accessToken.firstOrNull().orEmpty()}"

    override suspend fun getReservations(
        status: String?,
        startDate: String?,
        endDate: String?
    ): Result<List<ReservationOwner>> {
        return try {
            val response = api.getOwnerReservations(
                token = bearerToken(),
                status = status,
                startDate = startDate,
                endDate = endDate
            )
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

    override suspend fun confirmCashPayment(id: String): Result<Unit> {
        return try {
            val response = api.confirmCashPayment(
                token = bearerToken(),
                reservationId = id
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun checkIn(id: String): Result<ReservationOwner> {
        return try {
            val response = api.checkIn(token = bearerToken(),id)

            if (response.isSuccessful) {
                val reservation = response.body()?.toDomain()
                Result.success(reservation!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkOut(id: String): Result<ReservationOwner> {
        return try {
            val response = api.checkOut(token = bearerToken(),id)

            if (response.isSuccessful) {
                val reservation = response.body()?.toDomain()
                Result.success(reservation!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}