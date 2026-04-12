package com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper

import android.util.Log
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.models.ParkingLotDTO
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker

fun ParkingLotDTO.toDomain(): ParkingLot {
    return ParkingLot(
        id = id,
        name = name,
        description = description,
        address = address,
        city = city,
        state = state,
        latitude = latitude.toDouble(),
        longitude = longitude.toDouble(),
        totalSpots = totalSpots,
        availableSpots = availableSpots,
        pricePerHour = basePricePerHour.toDouble(),
        features = features,
        images = images,
        rating = ratingAverage.toDouble(),
        totalReviews = totalReviews,
        ownerName = owner.fullName
    )
}

fun ParkingLotDTO.toMarker() = ParkingMarker(
    id = id,
    name = name,
    address = address,
    latitude = latitude.toDoubleOrNull() ?: 0.0,
    longitude = longitude.toDoubleOrNull() ?: 0.0,
    pricePerHour = basePricePerHour.toDoubleOrNull() ?: 0.0,
    availableSpots = availableSpots,
    rating = ratingAverage.toDoubleOrNull() ?: 0.0,
    imageUrl = images.firstOrNull()
).also {
    Log.d("ParkingDebug", "mapped: ${it.name} lat=${it.latitude} lng=${it.longitude}")
}