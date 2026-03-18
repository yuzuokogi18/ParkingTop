package com.example.parkingtop.core.network

import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ParkingApi {

    @Multipart
    @POST("v1/auth/register")
    suspend fun register(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("fullName") fullName: RequestBody,
        @Part("phone") phone: RequestBody?,
        @Part("role") role: RequestBody?,
        @Part profileImage: MultipartBody.Part?
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
}
