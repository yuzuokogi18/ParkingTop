package com.example.parkingtop.features.cliente.BusquedaClient.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import javax.inject.Inject

class ParkingRepositoryImpl @Inject constructor(
    private val api: ParkingApi
) : ParkingRepository {

    override suspend fun getParkingLots(): Result<List<ParkingLot>> {
        return try {
            val response = api.getNearbyParkings(latitude = 0.0, longitude = 0.0, radius = 10000)

            if (response.isSuccessful && response.body()?.data != null) {
                val parkings = response.body()!!.data!!.map { dto ->
                    ParkingLot(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        address = dto.address,
                        city = dto.city,
                        state = dto.state,
                        latitude = dto.latitude,
                        longitude = dto.longitude,
                        totalSpots = dto.totalSpots,
                        availableSpots = dto.availableSpots,
                        pricePerHour = dto.basePricePerHour,
                        features = emptyList(), // ParkingDTO no trae features, se asume vacío o se ajusta
                        images = dto.images,
                        rating = dto.ratingAverage,
                        totalReviews = 0, // Ajustar según necesidad
                        ownerName = "" // Ajustar según necesidad
                    )
                }
                Result.success(parkings)
            } else {
                Result.failure(Exception("Error al obtener estacionamientos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getParkingLotsToMarker(): Result<List<ParkingMarker>> {
        return try {
            val response = api.getNearbyParkings(latitude = 0.0, longitude = 0.0, radius = 10000)

            if (response.isSuccessful && response.body()?.data != null) {
                val markers = response.body()!!.data!!.map { dto ->
                    ParkingMarker(
                        id             = dto.id,
                        name           = dto.name,
                        address        = dto.address,
                        latitude       = dto.latitude,
                        longitude      = dto.longitude,
                        pricePerHour   = dto.basePricePerHour,
                        availableSpots = dto.availableSpots,
                        rating         = dto.ratingAverage,
                        imageUrl       = dto.images.firstOrNull()
                    )
                }
                Result.success(markers)
            } else {
                Result.failure(Exception("Error al obtener marcadores"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
