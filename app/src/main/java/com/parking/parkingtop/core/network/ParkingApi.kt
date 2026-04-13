package com.parking.parkingtop.core.network

import com.parking.parkingtop.core.network.model.*
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.models.ParkingLotDTO
import com.parking.parkingtop.features.cliente.CreateVehicleClient.data.datasources.models.CreateVehicleRequest
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.datasources.models.ParkingLotDetailDTO

import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationRequest
import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.CreateReservationResponseDTO
import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.ParkingSpotDTO
import com.parking.parkingtop.features.cliente.ReservaClient.data.datasources.models.ReservationVehicleDTO
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.models.ReservationDTO
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.models.UserDTO
import com.parking.parkingtop.features.cliente.Perfil.data.datasources.models.VehicleDTO
import com.parking.parkingtop.features.cliente.updateProfile.data.datasources.models.UpdateProfileRequest
import com.parking.parkingtop.features.propetario.crearestacionamiento.data.datasources.models.CreateParkingDto
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.HomePropetarioResponse
import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.ParkingDto as OwnerParkingDto
import com.parking.parkingtop.features.propetario.horariopropetario.data.datasources.models.AvailabilityResponseDto
import com.parking.parkingtop.features.propetario.reservationpropetario.data.datasources.models.ReservationOwnerDto
import com.parking.parkingtop.features.register.data.datasources.models.AuthResponseDTO
import com.parking.parkingtop.features.register.data.datasources.models.RegisterRequest
import com.parking.parkingtop.features.cliente.Reviews.data.datasources.models.CreateReviewRequest
import com.parking.parkingtop.features.cliente.Reviews.data.datasources.models.ReviewDTO
import com.parking.parkingtop.features.notifications.data.datasources.models.RegisterFcmTokenRequest
import com.parking.parkingtop.features.notifications.data.datasources.models.NotificationDTO
import com.parking.parkingtop.features.notifications.data.datasources.models.UnreadCountDTO
import com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.CreateParkingSpotRequest
import com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.DeleteMessageDto
import com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.ParkingSpotDto
import com.parking.parkingtop.features.propetario.misespaciospropetario.data.datasources.models.UpdateParkingSpotRequest
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
    @GET("v1/reservations/my")
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
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("totalSpots") totalSpots: RequestBody?,
        @Part("basePricePerHour") basePricePerHour: RequestBody?,
        @Part("overtimeRatePerHour") overtimeRatePerHour: RequestBody?,
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
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("totalSpots") totalSpots: RequestBody?,
        @Part("basePricePerHour") basePricePerHour: RequestBody?,
        @Part("overtimeRatePerHour") overtimeRatePerHour: RequestBody?,
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

    // ═══════════════════════════════════════
    // SUBSCRIPTIONS ENDPOINTS
    // ═══════════════════════════════════════
    @GET("v1/subscriptions/plans")
    suspend fun getPlans(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<SubscriptionPlanDto>>>

    @GET("v1/subscriptions/my-subscription")
    suspend fun getMySubscription(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserSubscriptionResponseDto>>

    @POST("v1/subscriptions")
    suspend fun createSubscription(
        @Header("Authorization") token: String,
        @Body body: CreateSubscriptionRequest
    ): Response<ApiResponse<CreateSubscriptionResultDto>>

    @PUT("v1/subscriptions/plan")
    suspend fun updatePlan(
        @Header("Authorization") token: String,
        @Body body: UpdateSubscriptionPlanRequest
    ): Response<ApiResponse<UserSubscriptionDto>>

    @POST("v1/subscriptions/cancel")
    suspend fun cancelSubscription(
        @Header("Authorization") token: String,
        @Body body: CancelSubscriptionRequest
    ): Response<ApiResponse<UserSubscriptionDto>>

    @POST("v1/subscriptions/reactivate")
    suspend fun reactivateSubscription(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserSubscriptionDto>>


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

    @GET("v1/reviews/parking/{parkingId}")
    suspend fun getParkingReviews(
        @Path("parkingId") parkingId: String
    ): Response<ApiResponse<List<ReviewDTO>>>

    // Protegido — requiere token
    @POST("v1/reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Body body: CreateReviewRequest
    ): Response<ApiResponse<ReviewDTO>>

    @GET("v1/notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Response<ApiResponse<List<NotificationDTO>>>

    @PUT("v1/notifications/{id}/read")
    suspend fun markAsRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>

    @PUT("v1/notifications/read-all")
    suspend fun markAllAsRead(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @POST("v1/notifications/fcm-token")
    suspend fun registerFcmToken(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>   // ← Map<String, String>
    ): Response<ApiResponse<Unit>>
}
