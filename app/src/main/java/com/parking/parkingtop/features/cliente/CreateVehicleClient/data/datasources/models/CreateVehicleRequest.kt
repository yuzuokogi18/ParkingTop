package com.parking.parkingtop.features.cliente.CreateVehicleClient.data.datasources.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Request — lo que enviamos al crear (sin id, vehicleType opcional)
@Serializable
data class CreateVehicleRequest(
    @SerialName("licensePlate") val licensePlate: String,
    @SerialName("brand")        val brand: String?,
    @SerialName("model")        val model: String?,
    @SerialName("color")        val color: String?,
    @SerialName("isDefault")    val isDefault: Boolean = false
)
// Nota: tu VehicleDTO existente se reutiliza como respuesta (ya lo tienes en ParkingApi)