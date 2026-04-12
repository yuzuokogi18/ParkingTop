package com.parking.parkingtop.features.propetario.homepropetario.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.GeneralSummary
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.OwnerParking
import com.parking.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class HomePropetarioRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : HomePropetarioRepository {

    override suspend fun getHomeData(): Result<HomePropetarioData> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val authHeader = "Bearer $token"

            val homeResponse = api.getHomeData(authHeader)
            val parkingsResponse = api.getOwnerParkings(authHeader)

            val realParkings = if (parkingsResponse.isSuccessful) {
                parkingsResponse.body()?.data?.map { dto ->
                    OwnerParking(
                        id = dto.id,
                        name = dto.name,
                        imageUrl = dto.imageUrl,
                        occupiedSpaces = dto.occupiedSpaces,
                        totalSpaces = dto.totalSpaces,
                        nextReservationsCount = dto.reservationsCount,
                        rating = dto.rating
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }

            if (homeResponse.isSuccessful) {
                val homeDto = homeResponse.body()?.data
                val finalData = HomePropetarioData(
                    ownerName = homeDto?.ownerName ?: "Propietario",
                    summary = homeDto?.summary?.toDomain() ?: GeneralSummary(0, 0.0),
                    notifications = homeDto?.notifications?.map { it.toDomain() } ?: emptyList(),
                    parkings = if (realParkings.isNotEmpty()) realParkings else homeDto?.parkings?.map { it.toDomain() }
                        ?: emptyList()
                )
                Result.success(finalData)
            } else {
                if (realParkings.isNotEmpty()) {
                    Result.success(
                        HomePropetarioData(
                            ownerName = "Bienvenido",
                            summary = GeneralSummary(0, 0.0),
                            notifications = emptyList(),
                            parkings = realParkings
                        )
                    )
                } else if (homeResponse.code() == 404) {
                    Result.success(
                        HomePropetarioData(
                            ownerName = "Bienvenido",
                            summary = GeneralSummary(0, 0.0),
                            notifications = emptyList(),
                            parkings = emptyList()
                        )
                    )
                } else {
                    Result.failure(Exception("Error al cargar datos del propietario"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
