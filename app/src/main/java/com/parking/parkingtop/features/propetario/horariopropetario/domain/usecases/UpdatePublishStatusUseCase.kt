package com.parking.parkingtop.features.propetario.horariopropetario.domain.usecases

import com.parking.parkingtop.features.propetario.horariopropetario.domain.repositories.AvailabilityRepository
import javax.inject.Inject

class UpdatePublishStatusUseCase @Inject constructor(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(isPublished: Boolean): Result<Unit> {
        return repository.updatePublishStatus(isPublished)
    }
}
