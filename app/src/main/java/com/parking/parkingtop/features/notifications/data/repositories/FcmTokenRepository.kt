package com.parking.parkingtop.features.notifications.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.notifications.data.datasources.models.RegisterFcmTokenRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenRepository @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) {
    suspend fun registerToken(fcmToken: String) {
        try {
            val accessToken = tokenDataStore.accessToken.first() ?: return
            api.registerFcmToken(
                token = "Bearer $accessToken",
                body  = RegisterFcmTokenRequest(token = fcmToken)
            )
        } catch (_: Exception) { }
    }
}
