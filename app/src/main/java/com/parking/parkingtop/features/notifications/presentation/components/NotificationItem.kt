package com.parking.parkingtop.features.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.parking.parkingtop.features.notifications.domain.entities.Notification
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val bgColor = if (notification.isRead) Color.White else Color(0xFFF0F7FF)

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isRead) 0.dp else 1.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono por tipo
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        colorForType(notification.type).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = iconForType(notification.type),
                    contentDescription = null,
                    tint               = colorForType(notification.type),
                    modifier           = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    notification.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    notification.message,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatTime(notification.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
            }

            // Punto azul si no está leída
            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(BlueSecondary, CircleShape)
                )
            }
        }
    }
}

private fun iconForType(type: String): ImageVector = when {
    type.contains("confirmed")  -> Icons.Default.CheckCircle
    type.contains("cancelled")  -> Icons.Default.Cancel
    type.contains("reminder")   -> Icons.Default.Alarm
    type.contains("payment")    -> Icons.Default.AttachMoney
    type.contains("overtime")   -> Icons.Default.AccessTime
    type.contains("approved")   -> Icons.Default.Verified
    else                        -> Icons.Default.Notifications
}

private fun colorForType(type: String): Color = when {
    type.contains("confirmed") || type.contains("payment_received") || type.contains("approved") ->
        Color(0xFF4CAF50)
    type.contains("cancelled") || type.contains("failed") ->
        Color(0xFFF44336)
    type.contains("reminder") || type.contains("overtime") ->
        Color(0xFFFF9800)
    else -> BlueSecondary
}

private fun formatTime(iso: String): String {
    // Formato simple — reemplaza por librería de fechas si necesitas más precisión
    return try {
        val parts = iso.split("T")
        if (parts.size == 2) "${parts[0]} ${parts[1].take(5)}"
        else iso
    } catch (_: Exception) { iso }
}