package com.parking.parkingtop.features.cliente.Reviews.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.cliente.Reviews.data.datasources.mappers.toDomain
import com.parking.parkingtop.features.cliente.Reviews.data.datasources.models.CreateReviewRequest
import com.parking.parkingtop.features.cliente.Reviews.domain.entities.Review
import com.parking.parkingtop.features.cliente.Reviews.domain.repositories.ReviewRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : ReviewRepository {

    private suspend fun token() = "Bearer ${
        tokenDataStore.accessToken.first() ?: throw Exception("Sin sesión")
    }"

    override suspend fun createReview(
        parkingLotId: String,
        reservationId: String,
        rating: Int,
        comment: String?
    ): Result<Review> = try {
        val response = api.createReview(
            token = token(),
            body  = CreateReviewRequest(
                parkingLotId = parkingLotId,
                reservationId = reservationId,
                rating = rating,
                comment = comment
            )
        )
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.toDomain())
        else Result.failure(Exception(response.body()?.error?.message ?: "Error al enviar reseña"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getParkingReviews(parkingLotId: String): Result<List<Review>> = try {
        val response = api.getParkingReviews(parkingLotId)
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.map { it.toDomain() })
        else Result.failure(Exception("Error al obtener reseñas"))
    } catch (e: Exception) { Result.failure(e) }
}

