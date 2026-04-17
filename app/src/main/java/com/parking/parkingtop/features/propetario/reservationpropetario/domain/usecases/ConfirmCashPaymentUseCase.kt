package com.parking.parkingtop.features.propetario.reservationpropetario.domain.usecases

import com.parking.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import javax.inject.Inject

class ConfirmCashPaymentUseCase @Inject constructor(
    private val repository: ReservationOwnerRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.confirmCashPayment(id)
    }
}