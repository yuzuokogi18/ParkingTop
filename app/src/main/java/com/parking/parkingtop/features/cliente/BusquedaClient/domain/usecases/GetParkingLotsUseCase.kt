package com.parking.parkingtop.features.cliente.BusquedaClient.domain.usecases

import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import javax.inject.Inject

class GetParkingLotsUseCase @Inject constructor(
    private val repository: ParkingRepository
) {

    suspend operator fun invoke(): List<ParkingLot> {
        return repository.getParkingLots()
    }
}