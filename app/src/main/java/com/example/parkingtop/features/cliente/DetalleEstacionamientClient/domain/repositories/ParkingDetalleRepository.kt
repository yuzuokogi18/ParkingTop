package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.repositories

import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot

interface ParkingDetalleRepository {

    suspend fun getParkingDetail(parkingId: String): Result<ParkingLot>

}