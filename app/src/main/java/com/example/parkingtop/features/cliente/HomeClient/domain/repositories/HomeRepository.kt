package com.example.parkingtop.features.cliente.HomeClient.domain.repositories

import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingLotDTO
import com.example.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.HomeClient.domain.entities.UserProfile

interface HomeRepository {
    suspend fun getNearbyParkings(lat: Double, lng: Double): Result<List<ParkingLot>>
    suspend fun getUserProfile(): Result<UserProfile>
}
