package com.example.parkingtop.features.cliente.EditVehicleClient.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateVehicleRequest(
    @SerialName("licensePlate") val licensePlate: String,
    @SerialName("brand")        val brand: String?,
    @SerialName("model")        val model: String?,
    @SerialName("color")        val color: String?,
    @SerialName("vehicleType")  val vehicleType: String,
    @SerialName("isDefault")    val isDefault: Boolean
)