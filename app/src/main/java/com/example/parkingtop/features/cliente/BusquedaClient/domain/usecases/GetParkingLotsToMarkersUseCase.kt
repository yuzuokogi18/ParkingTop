package com.example.parkingtop.features.cliente.BusquedaClient.domain.usecases

import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import javax.inject.Inject


class GetParkingLotsToMarkersUseCase @Inject constructor(
    private val repostiroy: ParkingRepository
) {

    suspend fun execute(): Result<List<ParkingMarker>> {
        return repostiroy.getParkingLotsToMarker()
    }
}