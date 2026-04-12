package com.parking.parkingtop.features.cliente.updateProfile.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.cliente.updateProfile.data.datasources.mappers.toUserProfile
import com.parking.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import com.parking.parkingtop.features.cliente.updateProfile.domain.entities.UserProfile
import com.parking.parkingtop.features.cliente.updateProfile.domain.repositories.UpdateProfileRepository
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UpdateProfileRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : UpdateProfileRepository {

    override suspend fun updateProfile(
        fullName: String,
        phone: String?,
        profileImage: File?
    ): Result<UserProfile> {
        return try {
            val token = tokenDataStore.accessToken.first()
                ?: return Result.failure(Exception("No hay sesión activa"))

            val response = if (profileImage != null) {
                // ── Multipart con imagen ──────────────────────────────────────
                val fullNamePart = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
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

                api.updateProfileWithImage(
                    token        = "Bearer $token",
                    fullName     = fullNamePart,
                    phone        = phonePart,
                    profileImage = imagePart
                )
            } else {
                // ── JSON sin imagen ───────────────────────────────────────────
                api.updateProfile(
                    token   = "Bearer $token",
                    request = UpdateProfileRequest(fullName = fullName, phone = phone)
                )
            }

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!.toUserProfile())
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al actualizar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}