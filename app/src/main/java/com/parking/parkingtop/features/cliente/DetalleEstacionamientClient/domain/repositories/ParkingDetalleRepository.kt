package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.repositories

import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot

interface ParkingDetalleRepository {

    suspend fun getParkingDetail(parkingId: String): Result<ParkingLot>

}