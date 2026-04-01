package com.example.parkingtop.features.propetario.reservationpropetario.domain.usecases

import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.example.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import javax.inject.Inject

class GetOwnerReservationsUseCase @Inject constructor(
    private val repository: ReservationOwnerRepository
) {
    suspend operator fun invoke(status: String? = null): Result<List<ReservationOwner>> {
        return repository.getReservations(status)
    }
}
