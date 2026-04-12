package com.parking.parkingtop.features.register.domain.usecases

import com.parking.parkingtop.features.register.domain.entities.AuthResult
import com.parking.parkingtop.features.register.domain.repositories.RegisterRepository
import java.io.File
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String,
        phone: String?,
        role: String?,
        profileImage: File?
    ): Result<AuthResult> {
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return repository.register(email, password, fullName, phone, role, profileImage)
    }
}
