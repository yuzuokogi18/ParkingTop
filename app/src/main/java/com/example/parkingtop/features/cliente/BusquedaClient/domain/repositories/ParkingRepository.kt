package com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories

import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker

interface ParkingRepository {

    suspend fun getParkingLots(): Result<List<ParkingLot>>

    suspend fun getParkingLotsToMarker(): Result<List<ParkingMarker>>
}