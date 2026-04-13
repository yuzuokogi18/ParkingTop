package com.parking.parkingtop.features.login.domain.repositories

import com.parking.parkingtop.core.network.model.UserSubscriptionDto
import com.parking.parkingtop.features.login.domain.entities.AuthResult
import com.parking.parkingtop.features.login.domain.entities.UserSubscription

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>

    suspend fun getMySubscription(token: String): Result<UserSubscription?>
}