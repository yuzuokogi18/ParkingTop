package com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases

import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import javax.inject.Inject

class GetDefaultVehicleUseCase @Inject constructor(
    private val repository: ReservationRepository
) {
    suspend fun execute(): Result<Vehicle?> = repository.getDefaultVehicle()
}