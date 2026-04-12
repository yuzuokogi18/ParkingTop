package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository

import javax.inject.Inject

class GetReservationsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Reservation>> {
        return repository.getReservations()
    }
}