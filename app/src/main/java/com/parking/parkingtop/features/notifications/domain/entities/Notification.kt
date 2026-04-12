package com.parking.parkingtop.features.notifications.domain.entities

data class Notification(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String,
    val reservationId: String?,
    val data: Map<String, String>
)