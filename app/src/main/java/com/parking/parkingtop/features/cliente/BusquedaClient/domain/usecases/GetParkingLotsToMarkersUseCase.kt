package com.parking.parkingtop.features.cliente.BusquedaClient.domain.usecases

import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import javax.inject.Inject


class GetParkingLotsToMarkersUseCase @Inject constructor(
    private val repository: ParkingRepository
) {
    suspend fun execute(): Result<List<ParkingMarker>> {
        return repository.getParkingLotsToMarker()
    }
}