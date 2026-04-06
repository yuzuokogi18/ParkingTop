package com.example.parkingtop.features.cliente.ReservaClient.domain.usecases

import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import javax.inject.Inject

class GetVehiclesForReservationUseCase @Inject constructor(
    private val repository: ReservationRepository
) {
    suspend fun execute(): Result<List<Vehicle>> = repository.getVehicles()
}
