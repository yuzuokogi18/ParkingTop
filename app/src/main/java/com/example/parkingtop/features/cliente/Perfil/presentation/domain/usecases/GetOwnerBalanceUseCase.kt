package com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.example.parkingtop.core.network.model.OwnerBalanceDto
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetOwnerBalanceUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<OwnerBalanceDto> {
        return repository.getOwnerBalance()
    }
}