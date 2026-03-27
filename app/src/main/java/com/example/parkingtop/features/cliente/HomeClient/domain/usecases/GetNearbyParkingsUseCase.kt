package com.example.parkingtop.features.cliente.HomeClient.domain.usecases

import com.example.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.HomeClient.domain.repositories.HomeRepository
import javax.inject.Inject

class GetNearbyParkingsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): Result<List<ParkingLot>> {
        return repository.getNearbyParkings(lat, lng)
    }
}
