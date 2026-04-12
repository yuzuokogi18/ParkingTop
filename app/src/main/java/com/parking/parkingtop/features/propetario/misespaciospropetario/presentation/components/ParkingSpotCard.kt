package com.parking.parkingtop.features.propetario.misespaciospropetario.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.ParkingSpot
import com.parking.parkingtop.features.propetario.misespaciospropetario.domain.entities.SpotStatus
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun ParkingSpotCard(
    spot: ParkingSpot,
    onClick: () -> Unit
) {
    val statusConfig = getStatusConfig(spot.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lugar #${spot.spotNumber}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        ),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${spot.floor ?: "Piso 1"} • ${spot.section ?: "Sección A"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                StatusBadge(statusConfig)
            }

            // Indicador de estado con punto y descripción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusConfig.color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = statusConfig.description,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = TextPrimary
                    )
                }

                if (spot.status == SpotStatus.OCCUPIED || spot.status == SpotStatus.RESERVED) {
                    val label = if (spot.status == SpotStatus.OCCUPIED) "En uso" else "15 min"
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = statusConfig.color,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusConfig.color.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Barra de progreso con degradado naranja (solo si está ocupado o reservado)
            if (spot.status == SpotStatus.OCCUPIED || spot.status == SpotStatus.RESERVED) {
                val progress = 0.7f // Simulado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3F4))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFFB74D), Color(0xFFFF9800))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(config: StatusConfig) {
    Surface(
        color = config.color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = config.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = config.color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

data class StatusConfig(
    val label: String,
    val description: String,
    val color: Color
)

private fun getStatusConfig(status: SpotStatus): StatusConfig {
    return when (status) {
        SpotStatus.AVAILABLE -> StatusConfig("Disponible", "Libre ahora", Color(0xFF34C759)) // Verde iOS
        SpotStatus.OCCUPIED -> StatusConfig("Ocupado", "En uso", Color(0xFFFF3B30))    // Rojo iOS
        SpotStatus.RESERVED -> StatusConfig("Reservado", "Reservado", Color(0xFFFFCC00)) // Amarillo iOS
        SpotStatus.MAINTENANCE -> StatusConfig("Mantenimiento", "No disponible", Color(0xFF8E8E93)) // Gris iOS
        else -> StatusConfig("Desconocido", "Estado desconocido", Color.LightGray)
    }
}
