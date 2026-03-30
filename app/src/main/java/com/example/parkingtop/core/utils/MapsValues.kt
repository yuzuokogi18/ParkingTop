package com.example.parkingtop.core.utils

import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds

object MapsValues {

    val SAN_CRISTOBAL = LatLng(16.7370, -92.6376)

    val SAN_CRISTOBAL_BOUNDS: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(16.7600, -92.6700))
        .include(LatLng(16.7100, -92.6000))
        .build()

    const val MAX_DISTANCE_KM = 5.0

    const val MAP_STYLE = "https://tiles.openfreemap.org/styles/liberty"

    const val MARKER_IMAGE_ID = "parking-marker"
    const val SOURCE_ID       = "parkings-source"
    const val LAYER_ID        = "parkings-layer"
    const val PROP_ID         = "parkingId"
    const val PROP_NAME       = "parkingName"

    fun isInsideSanCristobal(latitude: Double, longitude: Double): Boolean {
        val distanceKm = haversineKm(
            SAN_CRISTOBAL.latitude, SAN_CRISTOBAL.longitude,
            latitude, longitude
        )
        return distanceKm <= MAX_DISTANCE_KM
    }

    private fun haversineKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r    = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a    = Math.sin(dLat / 2).let { it * it } +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2).let { it * it }
        return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    }
}