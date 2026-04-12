package com.parking.parkingtop.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.parking.parkingtop.core.database.dao.ParkingDao
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toDomain
import com.parking.parkingtop.features.cliente.BusquedaClient.data.datasources.mapper.toEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ParkingSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val api: ParkingApi,
    private val parkingDao: ParkingDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val response = api.getParkingLots()

            if (response.isSuccessful && response.body()?.data != null) {
                val lots = response.body()!!.data!!.map { it.toDomain() }

                parkingDao.insertParkings(lots.map { it.toEntity() })

                parkingDao.deleteOldParkings(
                    System.currentTimeMillis() - 2 * 60 * 60 * 1000
                )

                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "parking_sync_worker"
    }
}