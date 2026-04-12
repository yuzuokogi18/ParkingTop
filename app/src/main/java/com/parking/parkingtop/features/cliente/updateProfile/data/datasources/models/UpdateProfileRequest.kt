package com.parking.parkingtop.features.cliente.updateProfile.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    @SerialName("fullName") val fullName: String,
    @SerialName("phone")    val phone: String?
)