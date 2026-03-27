package com.example.parkingtop.features.register.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.features.register.data.datasources.mapper.toDomain
import com.example.parkingtop.features.register.domain.entities.AuthResult
import com.example.parkingtop.features.register.domain.repositories.RegisterRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val json: Json
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
            // Convertimos cada campo a MultipartBody.Part para que coincida con la interfaz ParkingApi
            val emailPart = MultipartBody.Part.createFormData("email", email)
            val passwordPart = MultipartBody.Part.createFormData("password", password)
            val fullNamePart = MultipartBody.Part.createFormData("fullName", fullName)
            val phonePart = phone?.let { MultipartBody.Part.createFormData("phone", it) }
            val rolePart = MultipartBody.Part.createFormData("role", role ?: "customer")

            val imagePart = profileImage?.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
            }

            val response = api.register(
                emailPart,
                passwordPart,
                fullNamePart,
                phonePart,
                rolePart,
                imagePart
            )

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                val errorBody = response.errorBody()?.string()
                val message = try {
                    val apiError = json.decodeFromString<ApiResponse<Unit>>(errorBody ?: "")
                    apiError.error?.message ?: response.message()
                } catch (e: Exception) {
                    errorBody ?: response.message() ?: "Error desconocido"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
