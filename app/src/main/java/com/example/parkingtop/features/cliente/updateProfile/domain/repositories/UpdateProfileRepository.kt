package com.example.parkingtop.features.cliente.updateProfile.domain.repositories


import com.example.parkingtop.features.cliente.updateProfile.domain.entities.UserProfile
import java.io.File

interface UpdateProfileRepository {
    suspend fun updateProfile(
        fullName: String,
        phone: String?,
        profileImage: File?
    ): Result<UserProfile>
}