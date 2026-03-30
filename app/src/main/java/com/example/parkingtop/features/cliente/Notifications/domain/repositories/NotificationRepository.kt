package com.example.parkingtop.features.cliente.Notifications.domain.repositories

import com.example.parkingtop.features.cliente.Notifications.domain.entities.AppNotification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<AppNotification>>
    suspend fun getUnreadCount(): Result<Int>
    suspend fun markAsRead(id: String): Result<Unit>
    suspend fun markAllAsRead(): Result<Int>
}