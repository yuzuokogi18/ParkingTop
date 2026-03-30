package com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<Vehicle>> {
        return repository.getVehicles()
    }
}