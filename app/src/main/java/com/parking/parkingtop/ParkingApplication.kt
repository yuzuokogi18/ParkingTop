package com.parking.parkingtop

import android.app.Application
import androidx.work.Configuration
import com.google.firebase.messaging.FirebaseMessaging
import com.parking.parkingtop.core.notifications.NotificationChannels
import com.parking.parkingtop.features.notifications.data.repositories.FcmTokenRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import javax.inject.Inject

@HiltAndroidApp
class ParkingTopApplication : Application(), Configuration.Provider {

    @Inject override lateinit var workManagerConfiguration: Configuration
    @Inject lateinit var fcmTokenRepository: FcmTokenRepository

    private val appScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
        NotificationChannels.createAll(this)
    }
}