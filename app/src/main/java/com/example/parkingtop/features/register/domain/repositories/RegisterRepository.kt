package com.example.parkingtop.features.register.domain.repositories

import com.example.parkingtop.features.register.domain.entities.AuthResult
import java.io.File

interface RegisterRepository {
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String?,
        role: String?,
        profileImage: File?
    ): Result<AuthResult>
}
