package com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}