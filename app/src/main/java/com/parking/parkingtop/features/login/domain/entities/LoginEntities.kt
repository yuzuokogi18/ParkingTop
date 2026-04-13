package com.parking.parkingtop.features.login.domain.entities

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val role: String,
    val profileImageUrl: String? = null
)

data class AuthResult(
    val user: User,
    val token: String,
    val refreshToken: String? = null
)

data class UserSubscription(
    val id: String,
    val status: String,
    val plan: String,
    val currentPeriodEnd: String?
)