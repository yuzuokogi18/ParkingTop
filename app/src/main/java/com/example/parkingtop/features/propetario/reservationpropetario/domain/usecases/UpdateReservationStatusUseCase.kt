package com.example.parkingtop.features.propetario.reservationpropetario.domain.usecases

import com.example.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.example.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import javax.inject.Inject

class UpdateReservationStatusUseCase @Inject constructor(
    private val repository: ReservationOwnerRepository
) {
    suspend operator fun invoke(id: String, status: ReservationStatus): Result<Unit> {
        return repository.updateStatus(id, status)
    }
}
