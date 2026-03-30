package com.example.parkingtop.features.cliente.BusquedaClient.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toDomain
import com.example.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toMarker
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import javax.inject.Inject

class ParkingRepositoryImpl @Inject constructor(
    private val api: ParkingApi
) : ParkingRepository {

    override suspend fun getParkingLots(): Result<List<ParkingLot>> {
        return try {

            val response = api.getParkingLots()

            if (response.isSuccessful && response.body()?.data != null) {

                val parkings = response.body()!!.data!!.map {
                    it.toDomain()
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
}