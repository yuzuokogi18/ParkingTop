package com.example.parkingtop.features.propetario.misespaciospropetario.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.entities.SpotStatus
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun ParkingSpotCard(
    spot: ParkingSpot,
    onClick: () -> Unit
) {
    val statusConfig = getStatusConfig(spot.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Lugar #${spot.spotNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${spot.floor ?: "Piso 1"} • ${spot.section ?: "Sección A"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                StatusBadge(statusConfig)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusConfig.color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusConfig.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }

                if (spot.status == SpotStatus.OCCUPIED || spot.status == SpotStatus.RESERVED) {
                    Text(
                        text = if (spot.status == SpotStatus.OCCUPIED) "En uso" else "Reservado",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = statusConfig.color,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusConfig.color.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (spot.status == SpotStatus.OCCUPIED || spot.status == SpotStatus.RESERVED) {
                // Barra de progreso horizontal (naranja degradado simulado)
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFFF9800),
                    trackColor = Color(0xFFF1F3F4),
                )
            }
        }
    }
}

@Composable
fun StatusBadge(config: StatusConfig) {
    Surface(
        color = config.color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(config.color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = config.label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = config.color
            )
        }
    }
}

data class StatusConfig(
    val label: String,
    val description: String,
    val color: Color
)

private fun getStatusConfig(status: SpotStatus): StatusConfig {
    return when (status) {
        SpotStatus.AVAILABLE -> StatusConfig("Disponible", "Libre ahora", Color(0xFF4CAF50))
        SpotStatus.OCCUPIED -> StatusConfig("Ocupado", "En uso", Color(0xFFF44336))
        SpotStatus.RESERVED -> StatusConfig("Reservado", "Reservado", Color(0xFFFFC107))
        SpotStatus.MAINTENANCE -> StatusConfig("Mantenimiento", "No disponible", Color(0xFF9E9E9E))
        else -> StatusConfig("Desconocido", "Estado desconocido", Color.LightGray)
    }
}
