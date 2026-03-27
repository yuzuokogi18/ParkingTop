package com.example.parkingtop.core.network

import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.UserProfileDTO
import com.example.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import com.example.parkingtop.features.register.data.datasources.models.RegisterRequest
import com.example.parkingtop.features.register.data.datasources.models.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ParkingApi {

    @POST("v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @POST("v1/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body email: Map<String, String>
    ): Response<ApiResponse<Unit>>

    @GET("v1/parkings/nearby")
    suspend fun getNearbyParkings(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 5000
    ): Response<ApiResponse<List<ParkingDTO>>>

    @GET("v1/auth/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserProfileDTO>>
}