// core/network/ParkingApi.kt - ACTUALIZACIÓN COMPLETA PARA PROPIETARIOS
package com.example.parkingtop.core.network

import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.cliente.BusquedaClient.data.datasources.models.ParkingLotDTO
import com.example.parkingtop.features.cliente.CreateVehicleClient.data.datasources.models.CreateVehicleRequest
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.MarkAllReadDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.NotificationDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.UnreadCountDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.ReservationDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.UserDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO
import com.example.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import com.example.parkingtop.features.propetario.horariopropetario.data.datasources.models.AvailabilityResponseDto
import com.example.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationOwnerDto
import com.example.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import com.example.parkingtop.features.register.data.datasources.models.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ParkingApi {

    // ═══════════════════════════════════════
    // AUTH ENDPOINTS
    // ═══════════════════════════════════════
    @Multipart
    @POST("v1/auth/register")
    suspend fun registerWithImage(
        @Part("email")    email:        RequestBody,
        @Part("password") password:     RequestBody,
        @Part("fullName") fullName:     RequestBody,
        @Part("phone")    phone:        RequestBody?,
        @Part("role")     role:         RequestBody,
        @Part            profileImage:  MultipartBody.Part
    ): Response<ApiResponse<AuthResponseDTO>>

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

    @GET("v1/auth/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserDTO>>

    @PUT("v1/auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<UserDTO>>

    @Multipart
    @PUT("v1/auth/profile")
    suspend fun updateProfileWithImage(
        @Header("Authorization") token: String,
        @Part("fullName")      fullName: RequestBody,
        @Part("phone")         phone: RequestBody?,
        @Part                  profileImage: MultipartBody.Part
    ): Response<ApiResponse<UserDTO>>

    // ═══════════════════════════════════════
    // VEHICLES ENDPOINTS
    // ═══════════════════════════════════════

    @GET("v1/vehicles")
    suspend fun getVehicles(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<VehicleDTO>>>

    @POST("v1/vehicles")
    suspend fun createVehicle(
        @Header("Authorization") token: String,
        @Body vehicle: CreateVehicleRequest
    ): Response<ApiResponse<VehicleDTO>>

    @PUT("v1/vehicles/{id}")
    suspend fun updateVehicle(
        @Header("Authorization") token: String,
        @Path("id") vehicleId: String,
        @Body vehicle: VehicleDTO
    ): Response<ApiResponse<VehicleDTO>>

    @DELETE("v1/vehicles/{id}")
    suspend fun deleteVehicle(
        @Header("Authorization") token: String,
        @Path("id") vehicleId: String
    ): Response<ApiResponse<Unit>>

    // ═══════════════════════════════════════
    // RESERVATIONS ENDPOINTS
    // ═══════════════════════════════════════

    @GET("v1/reservations")
    suspend fun getReservations(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Response<ApiResponse<List<ReservationDTO>>>

    @GET("v1/reservations/{id}")
    suspend fun getReservationById(
        @Header("Authorization") token: String,
        @Path("id") reservationId: String
    ): Response<ApiResponse<ReservationDTO>>

    @PUT("v1/reservations/{id}/cancel")
    suspend fun cancelReservation(
        @Header("Authorization") token: String,
        @Path("id") reservationId: String,
        @Body reason: Map<String, String>?
    ): Response<ApiResponse<ReservationDTO>>

    // ═══════════════════════════════════════
    // PARKING LOTS ENDPOINTS
    // ═══════════════════════════════════════

    @GET("v1/parkings/nearby")
    suspend fun getNearbyParkings(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 5000
    ): Response<ApiResponse<List<ParkingDTO>>>

    @GET("v1/parkings/parking-lots")
    suspend fun getParkingLots(): Response<ApiResponse<List<ParkingLotDTO>>>

    @GET("v1/parkings/{parkingId}")
    suspend fun getParkingById(
        @Path("parkingId") parkingId: String
    ): Response<ApiResponse<ParkingLotDetailDTO>>

    // ENDPOINTS PARA PROPIETARIOS
    @Multipart
    @POST("v1/parkings")
    suspend fun createParking(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("totalSpots") totalSpots: RequestBody,
        @Part("basePricePerHour") basePrice: RequestBody,
        @Part("overtimeRatePerHour") overtimeRate: RequestBody,
        @Part("features") features: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<ApiResponse<Unit>>

    @PUT("v1/parkings/{id}")
    suspend fun updateParking(
        @Header("Authorization") token: String,
        @Path("id") parkingId: String,
        @Body data: RequestBody
    ): Response<ApiResponse<Unit>>

    @DELETE("v1/parkings/{id}")
    suspend fun deleteParking(
        @Header("Authorization") token: String,
        @Path("id") parkingId: String
    ): Response<ApiResponse<Unit>>

    @GET("v1/parkings/owner/my-parkings")
    suspend fun getOwnerParkings(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ParkingDTO>>>

    // RESERVACIONES PARA PROPIETARIO
    @GET("v1/owner/reservations")
    suspend fun getOwnerReservations(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Response<ApiResponse<List<ReservationOwnerDto>>>

    @PATCH("v1/owner/reservations/{id}/status")
    suspend fun updateReservationStatus(
        @Header("Authorization") token: String,
        @Path("id") reservationId: String,
        @Body status: Map<String, String>
    ): Response<ApiResponse<Unit>>

    // DISPONIBILIDAD Y HORARIO
    @GET("v1/owner/availability")
    suspend fun getAvailability(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AvailabilityResponseDto>>

    @PATCH("v1/owner/availability/publish")
    suspend fun updatePublishStatus(
        @Header("Authorization") token: String,
        @Body status: Map<String, Boolean>
    ): Response<ApiResponse<Unit>>

    // ═══════════════════════════════════════
    // NOTIFICATIONS ENDPOINTS
    // ═══════════════════════════════════════
    @GET("v1/notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<NotificationDTO>>>

    @GET("v1/notifications/unread-count")
    suspend fun getUnreadCount(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UnreadCountDTO>>

    @PUT("v1/notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<NotificationDTO>>

    @PUT("v1/notifications/read-all")
    suspend fun markAllNotificationsAsRead(
        @Header("Authorization") token: String
    ): Response<ApiResponse<MarkAllReadDTO>>

}
