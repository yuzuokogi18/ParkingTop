package com.parking.parkingtop.features.cliente.BusquedaClient.data.repositories

import com.parking.parkingtop.core.database.dao.ParkingDao
import com.parking.parkingtop.core.database.dao.UserLocationDao
import com.parking.parkingtop.core.database.entities.UserLocationEntity
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toEntity
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toMarker
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class ParkingRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val parkingDao: ParkingDao,
    private val userLocationDao: UserLocationDao
) : ParkingRepository {

    override suspend fun getParkingLots(): List<ParkingLot> {
        return try {
            // 1. Intenta el API
            val response = api.getParkingLots()
            if (response.isSuccessful && response.body()?.data != null) {
                val lots = response.body()!!.data!!.map { it.toDomain() }

                // 2. Guarda en Room (REPLACE — actualiza si ya existe)
                parkingDao.insertParkings(lots.map { it.toEntity() })

                // 3. Limpia cache antiguo (más de 1 hora)
                parkingDao.deleteOldParkings(System.currentTimeMillis() - 60 * 60 * 1000)

                lots
            } else {
                // API falló → devuelve lo que haya en Room
                getCachedParkingLots()
            }
        } catch (e: Exception) {
            // Sin internet → devuelve cache
            getCachedParkingLots()
        }
    }

    override suspend fun getParkingLotsToMarker(): Result<List<ParkingMarker>> {
        return try {

            val response = api.getParkingLots()

            if (response.isSuccessful && response.body()?.data != null) {

                val parkings = response.body()!!.data!!.map {
                    it.toMarker()
                }

                Result.success(parkings)

            } else {
                Result.failure(Exception("Error al obtener estacionamientos"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─────────────────────────────────────────
    // Obtener cache de Room
    // ─────────────────────────────────────────
    private suspend fun getCachedParkingLots(): List<ParkingLot> {

        val entities = parkingDao
            .getAllParkings()
            .first()

        return entities.map { it.toDomain() }
    }

    // ─────────────────────────────────────────
    // Flow de parkings cercanos
    // ─────────────────────────────────────────
    override fun getNearbyParkingsFlow(
        lat: Double,
        lng: Double
    ): Flow<List<ParkingLot>> {

        return parkingDao
            .getNearbyParkings(lat, lng)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    // ─────────────────────────────────────────
    // Guardar ubicación del usuario
    // ─────────────────────────────────────────
    override suspend fun saveUserLocation(
        lat: Double,
        lng: Double,
        accuracy: Float
    ) {

        userLocationDao.saveLocation(
            UserLocationEntity(
                latitude = lat,
                longitude = lng,
                accuracy = accuracy
            )
        )
    }

    // ─────────────────────────────────────────
    // Obtener última ubicación
    // ─────────────────────────────────────────
    override suspend fun getLastUserLocation(): UserLocationEntity? {
        return userLocationDao.getLastLocation()
    }
}