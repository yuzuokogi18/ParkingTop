package com.parking.parkingtop.features.notifications.data.datasources.models

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

@Serializable
data class RegisterFcmTokenRequest(
    @SerialName("token")    val token: String,
    @SerialName("platform") val platform: String = "android"
)

@Serializable
data class UnreadCountDTO(
    @SerialName("count") val count: Int
)