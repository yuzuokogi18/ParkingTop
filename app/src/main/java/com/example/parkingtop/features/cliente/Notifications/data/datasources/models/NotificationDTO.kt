package com.example.parkingtop.features.cliente.Notifications.data.datasources.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDTO(
    @SerialName("id")             val id: String,
    @SerialName("type")           val type: String,
    @SerialName("title")          val title: String,
    @SerialName("message")        val message: String,
    @SerialName("isRead")         val isRead: Boolean,
    @SerialName("createdAt")      val createdAt: String,
    @SerialName("reservationId")  val reservationId: String? = null,
    @SerialName("subscriptionId") val subscriptionId: String? = null
)

@Serializable
data class UnreadCountDTO(
    @SerialName("count") val count: Int
)

@Serializable
data class MarkAllReadDTO(
    @SerialName("count")   val count: Int,
    @SerialName("message") val message: String
)