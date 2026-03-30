package com.example.parkingtop.features.cliente.BusquedaClient.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParkingLotDTO(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("address")
    val address: String,

    @SerialName("city")
    val city: String,

    @SerialName("state")
    val state: String,

    @SerialName("latitude")
    val latitude: String,

    @SerialName("longitude")
    val longitude: String,

    @SerialName("totalSpots")
    val totalSpots: Int,

    @SerialName("availableSpots")
    val availableSpots: Int,

    @SerialName("basePricePerHour")
    val basePricePerHour: String,

    @SerialName("features")
    val features: List<String> = emptyList(),

    @SerialName("images")
    val images: List<String> = emptyList(),

    @SerialName("ratingAverage")
    val ratingAverage: String,

    @SerialName("totalReviews")
    val totalReviews: Int,

    @SerialName("owner")
    val owner: OwnerDTO
)

@Serializable
data class OwnerDTO(
    @SerialName("id")
    val id: String,

    @SerialName("fullName")
    val fullName: String
)