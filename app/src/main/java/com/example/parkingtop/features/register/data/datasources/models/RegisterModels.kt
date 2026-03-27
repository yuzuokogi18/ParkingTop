package com.example.parkingtop.features.register.data.datasources.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
    @SerialName("profileImageUrl") // Coincide con tu descripción de la API
    val profileImageUrl: String? = null,
    val isActive: Boolean = true,
    val emailVerified: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
