package com.parking.parkingtop.features.cliente.Reviews.domain.repositories

import com.parking.parkingtop.features.cliente.Reviews.domain.entities.Review

interface ReviewRepository {
    suspend fun createReview(
        parkingLotId: String,
        reservationId: String,
        rating: Int,
        comment: String?
    ): Result<Review>

    suspend fun getParkingReviews(parkingLotId: String): Result<List<Review>>
}