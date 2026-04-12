package com.parking.parkingtop

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import javax.inject.Inject

@HiltAndroidApp
class ParkingTopApplication : Application(), Configuration.Provider {
    @Inject
    override lateinit var workManagerConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
    }
}