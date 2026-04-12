package com.parking.parkingtop.features.propetario.homepropetario.domain.repositories

import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData

interface HomePropetarioRepository {
    suspend fun getHomeData(): Result<HomePropetarioData>
}
