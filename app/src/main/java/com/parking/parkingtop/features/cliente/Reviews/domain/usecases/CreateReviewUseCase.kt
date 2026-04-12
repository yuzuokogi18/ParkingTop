package com.parking.parkingtop.features.cliente.Reviews.domain.usecases

import com.parking.parkingtop.features.cliente.Reviews.domain.entities.Review
import com.parking.parkingtop.features.cliente.Reviews.domain.repositories.ReviewRepository
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend fun execute(
        parkingLotId: String,
        reservationId: String,
        rating: Int,
        comment: String?
    ): Result<Review> {
        if (rating < 1 || rating > 5) {
            return Result.failure(Exception("El rating debe ser entre 1 y 5"))
        }
        return repository.createReview(parkingLotId, reservationId, rating, comment)
    }
}