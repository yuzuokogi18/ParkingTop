package com.example.parkingtop.features.register.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.register.data.datasources.mapper.toDomain
import com.example.parkingtop.features.register.domain.entities.AuthResult
import com.example.parkingtop.features.register.domain.repositories.RegisterRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

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
            val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val fullNamePart = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
            val phonePart = phone?.toRequestBody("text/plain".toMediaTypeOrNull())
            val rolePart = role?.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = profileImage?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
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
                Result.failure(Exception(response.message() ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
