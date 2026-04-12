package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases

import com.parking.parkingtop.core.network.model.OwnerBalanceDto
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetOwnerBalanceUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<OwnerBalanceDto> {
        return repository.getOwnerBalance()
    }
}