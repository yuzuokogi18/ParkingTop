package com.parking.parkingtop.features.notifications.domain.usecases

import com.parking.parkingtop.features.notifications.domain.repositories.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(private val repo: NotificationRepository) {
    suspend fun execute(page: Int = 1) = repo.getNotifications(page)
}