package com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories

import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner

interface ReservationOwnerRepository {
    suspend fun getReservations(
        status: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<ReservationOwner>>

    suspend fun confirmCashPayment(id: String): Result<Unit>

    suspend fun checkIn(id: String): Result<ReservationOwner>

    suspend fun checkOut(id: String): Result<ReservationOwner>
}