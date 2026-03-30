package com.example.parkingtop.features.cliente.Notifications.data.datasources.mappers

import com.example.parkingtop.features.cliente.Notifications.data.datasources.models.NotificationDTO
import com.example.parkingtop.features.cliente.Notifications.domain.entities.AppNotification

fun NotificationDTO.toDomain() = AppNotification (
    id            = id,
    type          = type,
    title         = title,
    message       = message,
    isRead        = isRead,
    createdAt     = createdAt,
    reservationId = reservationId
)