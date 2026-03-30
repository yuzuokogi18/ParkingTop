package com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.domain.repositories.ParkingDetalleRepository
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.mappers.toDomain
import javax.inject.Inject

class ParkingDetalleRepositoryImpl @Inject constructor(
    private val api: ParkingApi
) : ParkingDetalleRepository {

    override suspend fun getParkingDetail(parkingId: String): Result<ParkingLot> {
        return try {

            val response = api.getParkingById(parkingId)

            if (response.isSuccessful && response.body()?.data != null) {

                Result.success(
                    response.body()!!.data!!.toDomain()
                )

            } else {

                Result.failure(
                    Exception(response.body()?.error?.message ?: "Error al obtener el estacionamiento")
                )

            }

        } catch (e: Exception) {

            Result.failure(e)

        }
    }
}