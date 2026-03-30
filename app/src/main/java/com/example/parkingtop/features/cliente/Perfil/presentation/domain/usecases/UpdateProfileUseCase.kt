package com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases


import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(fullName: String, phone: String?): Result<User> {
        if (fullName.isBlank()) {
            return Result.failure(Exception("El nombre completo es requerido"))
        }
        return repository.updateProfile(fullName, phone)
    }
}