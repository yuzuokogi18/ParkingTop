package com.example.parkingtop.features.cliente.Notifications.domain.entities

data class AppNotification(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val createdAt: String,
    val reservationId: String?
)