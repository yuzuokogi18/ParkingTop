package com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories

import com.parking.parkingtop.core.network.model.OwnerBalanceDto
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle

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