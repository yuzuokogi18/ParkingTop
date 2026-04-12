package com.parking.parkingtop.core.workers

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParkingSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    /**
     * Programa la sincronización periódica.
     * - Se ejecuta cada 15 minutos (mínimo de WorkManager)
     * - Solo si hay red disponible
     * - Sobrevive a reinicios del dispositivo
     * - ExistingPeriodicWorkPolicy.KEEP → no reprograma si ya existe
     */
    fun schedule() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<ParkingSyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            ParkingSyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  // no reprograma si ya está corriendo
            syncRequest
        )
    }

    /**
     * Fuerza una sincronización inmediata (one-time) — útil al abrir la app
     */
    fun syncNow() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val immediateSync = OneTimeWorkRequestBuilder<ParkingSyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(immediateSync)
    }

    /**
     * Cancela el trabajo periódico
     */
    fun cancel() {
        workManager.cancelUniqueWork(ParkingSyncWorker.WORK_NAME)
    }
}
