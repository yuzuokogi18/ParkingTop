package com.parking.parkingtop.core.hardware.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.parking.parkingtop.core.hardware.data.GPSManager
import com.parking.parkingtop.core.hardware.domain.entities.LocationData
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AndroidGPSManager @Inject constructor(
    @ApplicationContext private val context: Context
) : GPSManager {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun isGPSEnabled(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): LocationData? {
        val location = fusedLocationClient.lastLocation.await()

        return location?.let {
            LocationData(
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }
}