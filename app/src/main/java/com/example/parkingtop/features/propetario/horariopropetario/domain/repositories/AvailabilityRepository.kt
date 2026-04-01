package com.example.parkingtop.features.propetario.horariopropetario.domain.repositories

import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.AvailabilityData

interface AvailabilityRepository {
    suspend fun getAvailability(): Result<AvailabilityData>
    suspend fun updatePublishStatus(isPublished: Boolean): Result<Unit>
}
