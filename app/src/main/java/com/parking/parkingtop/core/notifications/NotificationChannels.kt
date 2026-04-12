package com.parking.parkingtop.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {

    const val RESERVATIONS   = "reservations"
    const val PAYMENTS       = "payments"
    const val GENERAL        = "general"

    fun createAll(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        listOf(
            NotificationChannel(
                RESERVATIONS,
                "Reservas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Confirmaciones, recordatorios y alertas de reservas" },

            NotificationChannel(
                PAYMENTS,
                "Pagos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Confirmaciones y alertas de pago" },

            NotificationChannel(
                GENERAL,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Notificaciones generales de la app" }
        ).forEach { manager.createNotificationChannel(it) }
    }
}