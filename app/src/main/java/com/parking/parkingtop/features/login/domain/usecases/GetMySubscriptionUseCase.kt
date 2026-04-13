package com.parking.parkingtop.features.login.domain.usecases

import com.parking.parkingtop.features.login.domain.entities.UserSubscription
import com.parking.parkingtop.features.login.domain.repositories.LoginRepository
import javax.inject.Inject

class GetMySubscriptionUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(token: String): Result<UserSubscription?> {

        if (token.isBlank()) {
            return Result.failure(Exception("Token requerido"))
        }

        return repository.getMySubscription(token)
    }
}