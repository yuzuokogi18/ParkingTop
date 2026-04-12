package com.parking.parkingtop.features.propetario.crearestacionamiento.domain.repositories

import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData

interface CreateParkingRepository {
    suspend fun createParking(data: CreateParkingData): Result<Unit>
    suspend fun getParkingById(id: String): Result<CreateParkingData>
    suspend fun updateParking(id: String, data: CreateParkingData): Result<Unit>
    suspend fun deleteParking(id: String): Result<Unit>
}
