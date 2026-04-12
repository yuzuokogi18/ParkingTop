package com.parking.parkingtop.features.cliente.updateProfile.domain.usecases

import com.parking.parkingtop.features.cliente.updateProfile.domain.entities.UserProfile
import com.parking.parkingtop.features.cliente.updateProfile.domain.repositories.UpdateProfileRepository
import java.io.File
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: UpdateProfileRepository
) {
    suspend fun execute(
        fullName: String,
        phone: String?,
        profileImage: File?
    ): Result<UserProfile> {
        if (fullName.isBlank()) {
            return Result.failure(Exception("El nombre no puede estar vacío"))
        }
        return repository.updateProfile(fullName, phone, profileImage)
    }
}
