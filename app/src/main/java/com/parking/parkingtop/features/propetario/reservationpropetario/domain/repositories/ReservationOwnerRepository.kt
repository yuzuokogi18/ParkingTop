package com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories

import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus

interface ReservationOwnerRepository {
    suspend fun getReservations(status: String? = null): Result<List<ReservationOwner>>
    suspend fun updateStatus(id: String, status: ReservationStatus): Result<Unit>
}
