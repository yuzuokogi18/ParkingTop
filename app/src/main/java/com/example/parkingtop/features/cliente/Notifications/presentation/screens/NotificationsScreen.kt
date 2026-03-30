package com.example.parkingtop.features.cliente.Notifications.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parkingtop.features.cliente.Notifications.domain.entities.AppNotification
import com.example.parkingtop.features.cliente.Notifications.presentation.viewmodels.NotificationsViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Notificaciones",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimary
                            )
                            if (state.unreadCount > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                // ── Badge con conteo de no leídas ────────────
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(BlueSecondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text     = if (state.unreadCount > 99) "99+" else "${state.unreadCount}",
                                        color    = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBackIosNew, null, tint = TextPrimary)
                        }
                    },
                    actions = {
                        // Marcar todas como leídas
                        if (state.unreadCount > 0) {
                            TextButton(onClick = { viewModel.onMarkAllAsRead() }) {
                                Text("Leer todo", color = BlueSecondary, fontSize = 12.sp)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.notifications.isEmpty() -> {
                // ── Estado vacío ──────────────────────────────────────────────
                Box(
                    modifier         = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint     = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Sin notificaciones",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            "Aquí aparecerán tus notificaciones",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier       = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = state.notifications,
                        key   = { it.id }
                    ) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick      = {
                                if (!notification.isRead) {
                                    viewModel.onMarkAsRead(notification.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ── Item de notificación ──────────────────────────────────────────────────────
@Composable
private fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val bgColor = if (notification.isRead) Color.White else Color(0xFFF0F4FF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // ── Ícono según tipo ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(notificationColor(notification.type).copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = notificationIcon(notification.type),
                contentDescription = null,
                tint               = notificationColor(notification.type),
                modifier           = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = notification.title,
                    style      = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold
                    ),
                    color      = TextPrimary,
                    modifier   = Modifier.weight(1f)
                )
                // Punto azul si no leída
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BlueSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text  = notification.message,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text  = formatDate(notification.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )
        }
    }

    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
}

// ── Helpers de ícono y color por tipo ────────────────────────────────────────
@Composable
private fun notificationIcon(type: String): ImageVector = when (type) {
    "reservation_confirmed", "reservation_reminder" -> Icons.Outlined.EventAvailable
    "overtime_warning", "overtime_charged"          -> Icons.Outlined.Timer
    "payment_received", "payment_failed"            -> Icons.Outlined.CreditCard
    "subscription_expiring", "subscription_expired" -> Icons.Outlined.Subscriptions
    "parking_approved"                              -> Icons.Outlined.LocalParking
    else                                            -> Icons.Outlined.Notifications
}

private fun notificationColor(type: String): Color = when (type) {
    "reservation_confirmed"                         -> Color(0xFF22C55E)
    "reservation_reminder"                          -> Color(0xFF2563EB)
    "overtime_warning", "overtime_charged"          -> Color(0xFFF59E0B)
    "payment_received"                              -> Color(0xFF22C55E)
    "payment_failed"                                -> Color(0xFFEF4444)
    "subscription_expiring"                         -> Color(0xFFF59E0B)
    "subscription_expired"                          -> Color(0xFFEF4444)
    "parking_approved"                              -> Color(0xFF22C55E)
    else                                            -> Color(0xFF6B7280)
}

private fun formatDate(isoDate: String): String {
    return try {
        // Formato simple: "2024-01-15T10:30:00.000Z" → "15 ene, 10:30"
        val parts  = isoDate.split("T")
        val date   = parts[0].split("-")
        val time   = parts[1].substring(0, 5)
        val months = listOf("","ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic")
        "${date[2]} ${months[date[1].toInt()]}, $time"
    } catch (e: Exception) { isoDate }
}
