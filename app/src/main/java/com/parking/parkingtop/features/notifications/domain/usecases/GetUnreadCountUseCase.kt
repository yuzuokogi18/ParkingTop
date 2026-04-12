package com.parking.parkingtop.features.notifications.domain.usecases

import com.parking.parkingtop.features.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetUnreadCountUseCase @Inject constructor(private val repo: NotificationRepository) {
    suspend fun execute() = repo.getUnreadCount()
}