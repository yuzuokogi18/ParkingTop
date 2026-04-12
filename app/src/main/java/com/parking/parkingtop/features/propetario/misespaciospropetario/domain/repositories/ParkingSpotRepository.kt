package com.parking.parkingtop.features.propetario.misespaciospropetario.domain.repositories

import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot

interface ParkingSpotRepository {
    suspend fun getByParkingLotId(parkingLotId: String): Result<List<ParkingSpot>>
    suspend fun getById(id: String): Result<ParkingSpot>
    suspend fun delete(id: String): Result<Unit>
}
