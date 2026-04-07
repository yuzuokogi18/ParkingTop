package com.example.parkingtop.core.network

import com.example.parkingtop.core.network.model.ApiResponse
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.cliente.BusquedaClient.data.datasources.models.ParkingLotDTO
import com.example.parkingtop.features.cliente.CreateVehicleClient.data.datasources.models.CreateVehicleRequest
import com.example.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.ParkingDTO
import com.example.parkingtop.features.cliente.HomeClient.data.datasources.models.UserProfileDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.MarkAllReadDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.NotificationDTO
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationResponseDTO
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.ParkingSpotDTO
import com.example.parkingtop.features.cliente.ReservaClient.data.datasources.models.ReservationVehicleDTO
import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.UnreadCountDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.ReservationDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.UserDTO
import com.example.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO
import com.example.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import com.example.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.CreateParkingDto
import com.example.parkingtop.features.propetario.homepropetario.data.datasources.models.HomePropetarioResponse
import com.example.parkingtop.features.propetario.homepropetario.data.datasources.models.ParkingDto as OwnerParkingDto
import com.example.parkingtop.features.propetario.horariopropetario.data.datasources.models.AvailabilityResponseDto
import com.example.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationOwnerDto
import com.example.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import com.example.parkingtop.features.register.data.datasources.models.RegisterRequest
import com.example.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.*
import com.example.parkingtop.core.network.model.*
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
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponseDTO>>

    @POST("v1/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Unit>>

    @POST("v1/auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<ApiResponse<Unit>>

    @GET("v1/auth/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ApiResponse<UserDTO>>

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
    suspend fun getVehicles(@Header("Authorization") token: String): Response<ApiResponse<List<VehicleDTO>>>

    @POST("v1/vehicles")
    suspend fun createVehicle(@Header("Authorization") token: String, @Body vehicle: CreateVehicleRequest): Response<ApiResponse<VehicleDTO>>

    @PUT("v1/vehicles/{id}")
    suspend fun updateVehicle(
        @Header("Authorization") token: String,
        @Path("id") vehicleId: String,
        @Body vehicle: VehicleDTO
    ): Response<ApiResponse<VehicleDTO>>

    @DELETE("v1/vehicles/{id}")
    suspend fun deleteVehicle(@Header("Authorization") token: String, @Path("id") vehicleId: String): Response<ApiResponse<Unit>>

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
    suspend fun getParkingById(@Path("parkingId") parkingId: String): Response<ApiResponse<ParkingLotDetailDTO>>

    // ═══════════════════════════════════════
    // NOTIFICATIONS ENDPOINTS
    // ═══════════════════════════════════════
    @GET("v1/notifications")
    suspend fun getNotifications(@Header("Authorization") token: String): Response<ApiResponse<List<NotificationDTO>>>

    @GET("v1/notifications/unread-count")
    suspend fun getUnreadCount(@Header("Authorization") token: String): Response<ApiResponse<UnreadCountDTO>>

    @PUT("v1/notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<NotificationDTO>>

    @PUT("v1/notifications/read-all")
    suspend fun markAllNotificationsAsRead(@Header("Authorization") token: String): Response<ApiResponse<MarkAllReadDTO>>

    // ═══════════════════════════════════════
    // OWNER ENDPOINTS (PROPIETARIO)
    // ═══════════════════════════════════════
    @GET("v1/owner/home")
    suspend fun getHomeData(@Header("Authorization") token: String): Response<ApiResponse<HomePropetarioResponse>>

    @POST("v1/parkings")
    suspend fun createParking(@Header("Authorization") token: String, @Body request: CreateParkingDto): Response<ApiResponse<Unit>>

    @Multipart
    @POST("v1/parkings")
    suspend fun createParkingWithImages(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("postalCode") postalCode: RequestBody?,
        @Part("latitude") latitude: Double?,
        @Part("longitude") longitude: Double?,
        @Part("totalSpots") totalSpots: Int?,
        @Part("basePricePerHour") basePricePerHour: Double?,
        @Part("overtimeRatePerHour") overtimeRatePerHour: Double?,
        @Part("features") features: RequestBody?,
        @Part("operatingHours") operatingHours: RequestBody?,
        @Part images: List<MultipartBody.Part>?
    ): Response<ApiResponse<Unit>>

    @PUT("v1/parkings/{id}")
    suspend fun updateParking(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: CreateParkingDto
    ): Response<ApiResponse<Unit>>

    @Multipart
    @PUT("v1/parkings/{id}")
    suspend fun updateParkingWithImages(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("postalCode") postalCode: RequestBody?,
        @Part("latitude") latitude: Double?,
        @Part("longitude") longitude: Double?,
        @Part("totalSpots") totalSpots: Int?,
        @Part("basePricePerHour") basePricePerHour: Double?,
        @Part("overtimeRatePerHour") overtimeRatePerHour: Double?,
        @Part("features") features: RequestBody?,
        @Part("operatingHours") operatingHours: RequestBody?,
        @Part images: List<MultipartBody.Part>?
    ): Response<ApiResponse<Unit>>

    @DELETE("v1/parkings/{id}")
    suspend fun deleteParking(@Header("Authorization") token: String, @Path("id") id: String): Response<ApiResponse<Unit>>

    @GET("v1/parkings/owner/my-parkings")
    suspend fun getOwnerParkings(@Header("Authorization") token: String): Response<ApiResponse<List<OwnerParkingDto>>>

    @GET("v1/owner/reservations")
    suspend fun getOwnerReservations(@Header("Authorization") token: String, @Query("status") status: String? = null): Response<ApiResponse<List<ReservationOwnerDto>>>

    @PATCH("v1/owner/reservations/{id}/status")
    suspend fun updateReservationStatus(@Header("Authorization") token: String, @Path("id") reservationId: String, @Body status: Map<String, String>): Response<ApiResponse<Unit>>

    @GET("v1/owner/availability")
    suspend fun getAvailability(@Header("Authorization") token: String): Response<ApiResponse<AvailabilityResponseDto>>

    @PATCH("v1/owner/availability/publish")
    suspend fun updatePublishStatus(@Header("Authorization") token: String, @Body status: Map<String, Boolean>): Response<ApiResponse<Unit>>

    // ═══════════════════════════════════════
    // PARKING SPOTS ENDPOINTS
    // ═══════════════════════════════════════
    @GET("v1/parking-spots/parking-lot/{parkingLotId}")
    suspend fun getByParkingLotId(
        @Header("Authorization") bearerToken: String,
        @Path("parkingLotId") parkingLotId: String
    ): Response<ApiResponse<List<ParkingSpotDto>>>

    @GET("v1/parking-spots/{id}")
    suspend fun getParkingSpotById(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String
    ): Response<ApiResponse<ParkingSpotDto>>

    @POST("v1/parking-spots")
    suspend fun createParkingSpot(
        @Header("Authorization") bearerToken: String,
        @Body body: CreateParkingSpotRequest
    ): Response<ApiResponse<ParkingSpotDto>>

    @PUT("v1/parking-spots/{id}")
    suspend fun updateParkingSpot(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
        @Body body: UpdateParkingSpotRequest
    ): Response<ApiResponse<ParkingSpotDto>>

    @DELETE("v1/parking-spots/{id}")
    suspend fun deleteParkingSpot(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String
    ): Response<ApiResponse<DeleteMessageDto>>

    // ═══════════════════════════════════════
    // PAYOUTS ENDPOINTS
    // ═══════════════════════════════════════
    @GET("v1/payouts/balance")
    suspend fun getBalance(@Header("Authorization") token: String): Response<ApiResponse<OwnerBalanceDto>>

    @POST("v1/payouts/request")
    suspend fun requestPayout(
        @Header("Authorization") token: String,
        @Body body: RequestPayoutBody
    ): Response<ApiResponse<PayoutDto>>

    @GET("v1/payouts/history")
    suspend fun getPayoutHistory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 20
    ): Response<ApiResponse<PayoutListData>>


    @GET("v1/parking-spots/parking-lot/{parkingLotId}")
    suspend fun getParkingSpots(
        @Path("parkingLotId") parkingLotId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ParkingSpotDTO>>>

    @POST("v1/reservations")
    suspend fun createReservation(
        @Header("Authorization") token: String,
        @Body request: CreateReservationRequest
    ): Response<ApiResponse<CreateReservationResponseDTO>>


    @GET("v1/vehicles")
    suspend fun getVehiclesClient(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ReservationVehicleDTO>>>

    @GET("v1/vehicles/default")
    suspend fun getDefaultVehicle(
        @Header("Authorization") token: String
    ): Response<ApiResponse<ReservationVehicleDTO>>

}
