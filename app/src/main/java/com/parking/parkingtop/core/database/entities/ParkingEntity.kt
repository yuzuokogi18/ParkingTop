package com.parking.parkingtop.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parkings")
data class ParkingEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val description: String?,

    val address: String,

    val city: String,

    val state: String,

    val latitude: Double,

    val longitude: Double,

    val totalSpots: Int,

    val availableSpots: Int,

    val basePricePerHour: Double,

    val ratingAverage: Double,

    val totalReviews: Int,

    val ownerName: String,

    val images: String,     // JSON array

    val features: String,   // JSON array

    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_location")
data class UserLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long = System.currentTimeMillis()
)