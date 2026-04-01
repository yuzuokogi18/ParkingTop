package com.example.parkingtop.features.propetario.homepropetario.data.repositories

import com.example.parkingtop.features.propetario.homepropetario.data.datasources.HomePropetarioApi
import com.example.parkingtop.features.propetario.homepropetario.data.datasources.mapper.toDomain
import com.example.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.example.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
import javax.inject.Inject

class HomePropetarioRepositoryImpl @Inject constructor(
    private val api: HomePropetarioApi
) : HomePropetarioRepository {
    override suspend fun getHomeData(): Result<HomePropetarioData> {
        return try {
            val response = api.getHomeData()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
