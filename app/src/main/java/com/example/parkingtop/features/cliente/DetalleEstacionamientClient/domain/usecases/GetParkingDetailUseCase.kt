package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.usecases

import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.repositories.ParkingDetalleRepository
import javax.inject.Inject

class GetParkingDetailUseCase @Inject constructor(
   private val parkingRepository: ParkingDetalleRepository
) {

    suspend fun execute(idParking: String): Result<ParkingLot> {
        return parkingRepository.getParkingDetail(idParking)
    }
}