package com.example.parkingtop.features.propetario.misespaciospropetario.domain.usecases

import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.repositories.ParkingSpotRepository
import javax.inject.Inject

class GetParkingSpotByIdUseCase @Inject constructor(
    private val repository: ParkingSpotRepository
) {
    suspend operator fun invoke(id: String): Result<ParkingSpot> {
        return repository.getById(id)
    }
}
