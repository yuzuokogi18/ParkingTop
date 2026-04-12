package com.parking.parkingtop.features.notifications.domain.repositories

import com.parking.parkingtop.features.notifications.domain.entities.Notification


interface NotificationRepository {
    suspend fun getNotifications(page: Int = 1): Result<List<Notification>>
    suspend fun getUnreadCount(): Result<Int>
    suspend fun markAsRead(id: String): Result<Unit>
    suspend fun markAllAsRead(): Result<Unit>
}