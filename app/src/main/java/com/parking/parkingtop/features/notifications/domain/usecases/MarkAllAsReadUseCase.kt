package com.parking.parkingtop.features.notifications.domain.usecases

import com.parking.parkingtop.features.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(private val repo: NotificationRepository) {
    suspend fun execute(id: String) = repo.markAsRead(id)
}