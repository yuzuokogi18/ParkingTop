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
            val response = if (profileImage != null) {
                val emailPart    = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
                val namePart     = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
                val rolePart     = (role ?: "customer").toRequestBody("text/plain".toMediaTypeOrNull())
                val phonePart    = phone?.toRequestBody("text/plain".toMediaTypeOrNull())

                val mimeType = when {
                    profileImage.name.endsWith(".png")  -> "image/png"
                    profileImage.name.endsWith(".webp") -> "image/webp"
                    else -> "image/jpeg"
                }
                val imagePart = MultipartBody.Part.createFormData(
                    name     = "profileImage",
                    filename = profileImage.name,
                    body     = profileImage.asRequestBody(mimeType.toMediaTypeOrNull())
                )

                api.registerWithImage(
                    email        = emailPart,
                    password     = passwordPart,
                    fullName     = namePart,
                    phone        = phonePart,
                    role         = rolePart,
                    profileImage = imagePart
                )
            } else {

                api.register(
                    RegisterRequest(
                        email    = email,
                        password = password,
                        fullName = fullName,
                        phone    = phone,
                        role     = role
                    )
                )
            }

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toDomain())
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message() ?: "Error desconocido"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}