package com.example.parkingtop.features.cliente.ReservaClient.domain.repositories

import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ReservationResult
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle

interface ReservationRepository {
    suspend fun getVehicles(): Result<List<Vehicle>>
    suspend fun getDefaultVehicle(): Result<Vehicle?>
    suspend fun getParkingSpots(parkingLotId: String): Result<List<ParkingSpot>>  // ✅ nuevo
    suspend fun createReservation(request: CreateReservationRequest): Result<ReservationResult> // ✅ nuevo
}