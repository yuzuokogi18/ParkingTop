package com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper

import com.parking.parkingtop.core.database.entities.ParkingEntity
import com.parking.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingLot
import org.json.JSONArray

fun ParkingEntity.toDomain(): ParkingLot {

    val imagesList = try {
        val arr = JSONArray(images)
        (0 until arr.length()).map { arr.getString(it) }
    } catch (e: Exception) {
        emptyList()
    }

    val featuresList = try {
        val arr = JSONArray(features)
        (0 until arr.length()).map { arr.getString(it) }
    } catch (e: Exception) {
        emptyList()
    }

    return ParkingLot(
        id = id,
        name = name,
        description = description,
        address = address,
        city = city,
        state = state,
        latitude = latitude,
        longitude = longitude,
        totalSpots = totalSpots,
        availableSpots = availableSpots,
        pricePerHour = basePricePerHour,
        features = featuresList,
        images = imagesList,
        rating = ratingAverage,
        totalReviews = totalReviews,
        ownerName = ownerName
    )
}

fun ParkingLot.toEntity(): ParkingEntity = ParkingEntity(

    id = id,

    name = name,

    description = description,

    address = address,

    city = city,

    state = state,

    latitude = latitude,

    longitude = longitude,

    totalSpots = totalSpots,

    availableSpots = availableSpots,

    basePricePerHour = pricePerHour,

    ratingAverage = rating,

    totalReviews = totalReviews,

    ownerName = ownerName,

    images = JSONArray(images).toString(),

    features = JSONArray(features).toString(),

    cachedAt = System.currentTimeMillis()
)