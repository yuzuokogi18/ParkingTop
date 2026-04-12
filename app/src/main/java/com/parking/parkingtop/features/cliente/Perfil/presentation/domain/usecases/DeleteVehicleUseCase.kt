package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases


import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class DeleteVehicleUseCase @Inject constructor(
    private val repository: ProfileRepository   // el que ya tienes
) {
    suspend fun execute(vehicleId: String): Result<Unit> {
        if (vehicleId.isBlank()) return Result.failure(Exception("ID inválido"))
        return repository.deleteVehicle(vehicleId)
    }
}