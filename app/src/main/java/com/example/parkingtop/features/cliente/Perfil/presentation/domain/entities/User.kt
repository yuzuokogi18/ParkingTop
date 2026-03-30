package com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String?,
    val role: String,
    val profileImageUrl: String?,
    val isActive: Boolean,
    val emailVerified: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val licensePlate: String,
    val isDefault: Boolean
)

data class Reservation(
    val id: String,
    val parkingLotName: String,
    val parkingLotAddress: String,
    val startTime: String,
    val endTime: String,
    val totalCost: Double,
    val status: String,
    val reservationCode: String
)