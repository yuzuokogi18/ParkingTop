package com.parking.parkingtop.features.cliente.HomeClient.domain.repositories

import com.parking.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.User

interface HomeRepository {
    suspend fun getNearbyParkings(lat: Double, lng: Double): Result<List<ParkingLot>>
    suspend fun getUserProfile(): Result<User>
}
