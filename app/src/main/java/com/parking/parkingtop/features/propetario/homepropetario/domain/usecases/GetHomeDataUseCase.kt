package com.parking.parkingtop.features.propetario.homepropetario.domain.usecases

import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.parking.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomePropetarioRepository
) {
    suspend operator fun invoke(): Result<HomePropetarioData> {
        return repository.getHomeData()
    }
}
