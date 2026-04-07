package com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories

import com.example.parkingtop.core.network.model.OwnerBalanceDto
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import java.io.File

interface ProfileRepository {

    /**
     * Obtiene la información del perfil del usuario autenticado
     */
    suspend fun getProfile(): Result<User>

    /**
     * Actualiza nombre y teléfono del usuario
     */
    suspend fun updateProfile(
        fullName: String,
        phone: String?
    ): Result<User>

    /**
     * Obtiene los vehículos registrados del usuario
     */
    suspend fun getVehicles(): Result<List<Vehicle>>

    /**
     * Obtiene las reservas del usuario
     * status puede ser: active, completed, cancelled o null
     */
    suspend fun getReservations(
        status: String? = null
    ): Result<List<Reservation>>

    /**
     * Cierra sesión del usuario
     */
    suspend fun logout(): Result<Unit>

    suspend fun deleteVehicle(vehicleId: String): Result<Unit>

    /**
     * Obtiene el balance del propietario
     */
    suspend fun getOwnerBalance(): Result<OwnerBalanceDto>
}