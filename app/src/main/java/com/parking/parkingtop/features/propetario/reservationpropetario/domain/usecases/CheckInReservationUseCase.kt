package com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases

import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import javax.inject.Inject

class CheckInReservationUseCase @Inject constructor(
    private val repository: ReservationOwnerRepository
) {
    suspend operator fun invoke(id: String): Result<ReservationOwner> {
        return repository.checkIn(id)
    }
}