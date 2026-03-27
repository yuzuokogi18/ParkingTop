package com.example.parkingtop.features.register.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.register.data.datasources.mapper.toDomain
import com.example.parkingtop.features.register.data.datasources.models.RegisterRequest
import com.example.parkingtop.features.register.domain.entities.AuthResult
import com.example.parkingtop.features.register.domain.repositories.RegisterRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

// RegisterRepositoryImpl.kt
class RegisterRepositoryImpl @Inject constructor(
    private val api: ParkingApi
) : RegisterRepository {

    override suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String?,
        role: String?,
        profileImage: File?
    ): Result<AuthResult> {
        return try {
            // Crear objeto JSON en lugar de multipart
            val registerRequest = RegisterRequest(
                email = email,
                password = password,
                fullName = fullName,
                phone = phone,
                role = role
                // profileImage no se envía por ahora - se puede agregar después
            )

            val response = api.register(registerRequest)

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                Result.failure(Exception(response.message() ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}