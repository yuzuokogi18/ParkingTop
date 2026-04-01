package com.example.parkingtop.features.propetario.crearestacionamiento.domain.repositories

import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData

interface CreateParkingRepository {
    suspend fun createParking(data: CreateParkingData): Result<Unit>
}
