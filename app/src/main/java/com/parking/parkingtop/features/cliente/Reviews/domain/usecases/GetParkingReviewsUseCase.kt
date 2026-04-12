package com.parking.parkingtop.features.cliente.Reviews.domain.usecases

import com.parking.parkingtop.features.cliente.Reviews.domain.repositories.ReviewRepository
import javax.inject.Inject

class GetParkingReviewsUseCase {
    class GetParkingReviewsUseCase @Inject constructor(
        private val repository: ReviewRepository
    ) {
        suspend fun execute(parkingLotId: String) = repository.getParkingReviews(parkingLotId)
    }
}