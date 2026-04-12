package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Vehicle>> {
        return repository.getVehicles()
    }
}