package com.parking.parkingtop.features.notifications.data.repositories

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.notifications.data.datasources.mappers.toDomain
import com.parking.parkingtop.features.notifications.domain.entities.Notification
import com.parking.parkingtop.features.notifications.domain.repositories.NotificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: ParkingApi,
    private val tokenDataStore: TokenDataStore
) : NotificationRepository {

    private suspend fun token() = "Bearer ${
        tokenDataStore.accessToken.first() ?: throw Exception("Sin sesión")
    }"

    override suspend fun getNotifications(page: Int) = try {
        val r = api.getNotifications(token(), page)
        if (r.isSuccessful && r.body()?.data != null)
            Result.success(r.body()!!.data!!.map { it.toDomain() })
        else Result.failure(Exception("Error al cargar notificaciones"))
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getUnreadCount() = try {
        val r = api.getUnreadCount(token())
        if (r.isSuccessful && r.body()?.data != null)
            Result.success(r.body()!!.data!!.count)
        else Result.success(0)
    } catch (e: Exception) { Result.success(0) }

    override suspend fun markAsRead(id: String) = try {
        api.markAsRead(token(), id); Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun markAllAsRead() = try {
        api.markAllAsRead(token()); Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }
}