package com.parking.parkingtop.features.cliente.BusquedaClient.domain.repositories

import com.parking.parkingtop.core.database.entities.UserLocationEntity
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import kotlinx.coroutines.flow.Flow

interface ParkingRepository {

    suspend fun getParkingLots(): List<ParkingLot>

    suspend fun getParkingLotsToMarker(): Result<List<ParkingMarker>>

    fun getNearbyParkingsFlow(lat: Double, lng: Double): Flow<List<ParkingLot>>

    suspend fun saveUserLocation(lat: Double, lng: Double, accuracy: Float)

    suspend fun getLastUserLocation(): UserLocationEntity?
}