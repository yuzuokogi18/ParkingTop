package com.parking.parkingtop.features.notifications.data.datasources.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDTO(
    @SerialName("id")            val id: String,
    @SerialName("type")          val type: String,
    @SerialName("title")         val title: String,
    @SerialName("message")       val message: String,
    @SerialName("isRead")        val isRead: Boolean,
    @SerialName("createdAt")     val createdAt: String,
    @SerialName("reservationId") val reservationId: String? = null,
    @SerialName("data")          val data: Map<String, String>? = null
)

data class RegisterFcmTokenRequest(
    @SerializedName("token")    val token: String,
    @SerializedName("platform") val platform: String  // sin default, siempre requerido
)
@Serializable
data class UnreadCountDTO(
    @SerialName("count") val count: Int
)