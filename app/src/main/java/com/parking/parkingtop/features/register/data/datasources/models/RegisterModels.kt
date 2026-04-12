package com.parking.parkingtop.features.register.data.datasources.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDTO(
    val user: UserDTO,
    val token: String,
    val refreshToken: String? = null
)

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String? = null,
    val role: String,
    val profileImage: String? = null,
    val isActive: Boolean = true,
    val emailVerified: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String? = null,
    val role: String? = "customer"
)