package com.example.parkingtop.features.cliente.updateProfile.domain.entities

data class UserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String?,
    val profileImageUrl: String?
)
