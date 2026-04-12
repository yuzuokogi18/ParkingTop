package com.parking.parkingtop.features.register.domain.entities

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
    val refreshToken: String
)
