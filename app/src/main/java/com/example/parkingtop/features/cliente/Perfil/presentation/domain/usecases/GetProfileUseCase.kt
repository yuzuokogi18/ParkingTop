package com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.getProfile()
    }
}
