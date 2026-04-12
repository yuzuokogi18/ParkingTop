package com.parking.parkingtop.features.login.domain.usecases

import com.parking.parkingtop.features.login.domain.entities.AuthResult
import com.parking.parkingtop.features.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResult> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email y contraseña son requeridos"))
        }
        return repository.login(email, password)
    }
}
