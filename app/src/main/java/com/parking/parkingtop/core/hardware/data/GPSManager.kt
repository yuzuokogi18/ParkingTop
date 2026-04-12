package com.parking.parkingtop.core.hardware.data

import com.parking.parkingtop.core.hardware.domain.entities.LocationData

interface GPSManager {

    fun isGPSEnabled(): Boolean

    suspend fun getLastLocation(): LocationData?

}