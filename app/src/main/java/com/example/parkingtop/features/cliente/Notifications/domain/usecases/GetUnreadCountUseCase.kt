package com.example.parkingtop.features.cliente.Notifications.domain.usecases

import com.example.parkingtop.features.cliente.Notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetUnreadCountUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend fun execute() = repository.getUnreadCount()
}