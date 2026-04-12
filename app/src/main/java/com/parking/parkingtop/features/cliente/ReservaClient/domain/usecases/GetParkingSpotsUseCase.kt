package com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases

import com.parking.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import javax.inject.Inject

class GetParkingSpotsUseCase @Inject constructor(
    private val repository: ReservationRepository
) {
    suspend fun execute(parkingLotId: String) = repository.getParkingSpots(parkingLotId)
}