package com.example.parkingtop.core.network

import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingLotDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.UserDTO
import com.example.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ParkingApi {

    @Multipart
    @POST("v1/auth/register")
    suspend fun register(
        @Part email: MultipartBody.Part,
        @Part password: MultipartBody.Part,
        @Part fullName: MultipartBody.Part,
        @Part phone: MultipartBody.Part?,
        @Part role: MultipartBody.Part?,
        @Part profileImage: MultipartBody.Part?
    ): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponseDTO>>

    @GET("v1/users/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserDTO>>

    @GET("v1/parkings/nearby")
    suspend fun getNearbyParkings(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 5000
    ): Response<ApiResponse<List<ParkingLotDTO>>>
}
