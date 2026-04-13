package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class CancelReservationUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend fun execute(reservationId: String): Result<Unit> {
        if (reservationId.isBlank()) return Result.failure(Exception("ID inválido"))
        return repository.cancelReservation(reservationId)
    }
}