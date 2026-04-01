package com.example.parkingtop.features.propetario.homepropetario.domain.repositories

import com.example.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData

interface HomePropetarioRepository {
    suspend fun getHomeData(): Result<HomePropetarioData>
}
