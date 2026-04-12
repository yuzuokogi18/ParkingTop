package com.parking.parkingtop.features.propetario.crearestacionamiento.domain.usecases

import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import javax.inject.Inject

class CreateParkingUseCase @Inject constructor(
    private val repository: CreateParkingRepository
) {
    suspend operator fun invoke(data: CreateParkingData): Result<Unit> {
        return repository.createParking(data)
    }
}
