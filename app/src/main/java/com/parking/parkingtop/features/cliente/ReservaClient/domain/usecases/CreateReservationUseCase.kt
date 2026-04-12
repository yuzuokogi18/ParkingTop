package com.parking.parkingtop.features.cliente.ReservaClient.domain.usecases

import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.parking.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import javax.inject.Inject

class CreateReservationUseCase @Inject constructor(
    private val repository: ReservationRepository
) {
    suspend fun execute(request: CreateReservationRequest) = repository.createReservation(request)
}