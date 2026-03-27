package com.example.parkingtop.features.cliente.HomeClient.domain.usecases

import com.example.parkingtop.features.cliente.HomeClient.domain.entities.UserProfile
import com.example.parkingtop.features.cliente.HomeClient.domain.repositories.HomeRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getUserProfile()
    }
}
