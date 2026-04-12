package com.parking.parkingtop.features.notifications.data.datasources.mappers

import com.parking.parkingtop.features.notifications.data.datasources.models.NotificationDTO
import com.parking.parkingtop.features.notifications.domain.entities.Notification

fun NotificationDTO.toDomain() = Notification(
    id = id,
    type = type,
    title = title,
    message = message,
    isRead = isRead,
    createdAt = createdAt,
    reservationId = reservationId,
    data = data ?: emptyMap()
)