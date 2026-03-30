package com.example.parkingtop.features.cliente.Notifications.data.repositories

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.cliente.Notifications.data.datasources.mappers.toDomain
import com.example.parkingtop.features.cliente.Notifications.domain.entities.AppNotification
import com.example.parkingtop.features.cliente.Notifications.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : NotificationRepository {

    private suspend fun token() = "Bearer ${tokenDataStore.accessToken.first()
        ?: throw Exception("No hay sesión activa")}"

    override suspend fun getNotifications(): Result<List<AppNotification>> = try {
        val response = api.getNotifications(token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.map { it.toDomain() })
        else Result.failure(Exception("Error al obtener notificaciones"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getUnreadCount(): Result<Int> = try {
        val response = api.getUnreadCount(token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.count)
        else Result.failure(Exception("Error al obtener contador"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun markAsRead(id: String): Result<Unit> = try {
        val response = api.markNotificationAsRead(token(), id)
        if (response.isSuccessful) Result.success(Unit)
        else Result.failure(Exception("Error al marcar notificación"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun markAllAsRead(): Result<Int> = try {
        val response = api.markAllNotificationsAsRead(token())
        if (response.isSuccessful && response.body()?.data != null)
            Result.success(response.body()!!.data!!.count)
        else Result.failure(Exception("Error al marcar todas"))
    } catch (e: Exception) { Result.failure(e) }
}