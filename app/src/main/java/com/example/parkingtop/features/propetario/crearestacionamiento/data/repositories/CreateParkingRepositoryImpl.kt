package com.example.parkingtop.features.propetario.crearestacionamiento.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.entities.CreateParkingData
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CreateParkingRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : CreateParkingRepository {
    override suspend fun createParking(data: CreateParkingData): Result<Unit> {
        return try {
            val token = tokenDataStore.accessToken.firstOrNull() ?: ""
            val authHeader = "Bearer $token"

            val name = data.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val address = data.address.toRequestBody("text/plain".toMediaTypeOrNull())
            val city = data.city.toRequestBody("text/plain".toMediaTypeOrNull())
            val state = data.state.toRequestBody("text/plain".toMediaTypeOrNull())
            val latitude = data.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val longitude = data.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val totalSpots = data.totalSpots.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val basePrice = data.basePricePerHour.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val overtimeRate = data.overtimeRatePerHour.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            // Convert list of features to JSON array string
            val featuresJson = Json.encodeToString(data.features)
            val featuresBody = featuresJson.toRequestBody("application/json".toMediaTypeOrNull())

            val imageParts = data.images.map { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", file.name, requestFile)
            }

            val response = api.createParking(
                token = authHeader,
                name = name,
                address = address,
                city = city,
                state = state,
                latitude = latitude,
                longitude = longitude,
                totalSpots = totalSpots,
                basePrice = basePrice,
                overtimeRate = overtimeRate,
                features = featuresBody,
                images = imageParts
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
