package com.parking.parkingtop.features.notifications.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.notifications.data.datasources.models.RegisterFcmTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenRepository @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) {
    // ← scope propio del singleton, no se cancela con el ViewModel
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // FcmTokenRepository.kt
// FcmTokenRepository.kt
    suspend fun registerToken(fcmToken: String, accessToken: String? = null) {
        try {
            val token = accessToken ?: tokenDataStore.accessToken.first() ?: return
            android.util.Log.d("FCM_DEBUG", "Token al registrar: $token")

            api.registerFcmToken(
                token = "Bearer $token",
                body  = mapOf("token" to fcmToken, "platform" to "android")  // ← Map en lugar de DTO
            )
        } catch (e: Exception) {
            android.util.Log.e("FCM_DEBUG", "Error en registerToken: ${e.message}")
        }
    }
    fun registerTokenAsync(fcmToken: String, accessToken: String? = null) {
        android.util.Log.d("FCM_DEBUG", "Lanzando registerTokenAsync")
        scope.launch {
            android.util.Log.d("FCM_DEBUG", "Dentro del scope, llamando registerToken")
            registerToken(fcmToken, accessToken)
        }
    }
}