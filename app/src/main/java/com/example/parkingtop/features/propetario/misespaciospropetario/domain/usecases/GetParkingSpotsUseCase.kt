package com.example.parkingtop.features.propetario.misespaciospropetario.domain.usecases

import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.repositories.ParkingSpotRepository
import javax.inject.Inject

class GetParkingSpotsUseCase @Inject constructor(
    private val repository: ParkingSpotRepository
) {
    suspend operator fun invoke(parkingLotId: String): Result<List<ParkingSpot>> {
        return repository.getByParkingLotId(parkingLotId)
    }
}
