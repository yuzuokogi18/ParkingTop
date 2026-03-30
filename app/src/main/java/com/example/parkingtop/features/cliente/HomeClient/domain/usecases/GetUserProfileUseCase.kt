package com.example.parkingtop.features.cliente.HomeClient.domain.usecases

import com.example.parkingtop.features.cliente.HomeClient.domain.entities.UserProfile
import com.example.parkingtop.features.cliente.HomeClient.domain.repositories.HomeRepository
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.getUserProfile()
    }
}
