package com.example.parkingtop.features.propetario.horariopropetario.domain.usecases

import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.AvailabilityData
import com.example.parkingtop.features.propetario.horariopropetario.domain.repositories.AvailabilityRepository
import javax.inject.Inject

class GetAvailabilityUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(): Result<AvailabilityData> {
        return repository.getAvailability()
    }
}
