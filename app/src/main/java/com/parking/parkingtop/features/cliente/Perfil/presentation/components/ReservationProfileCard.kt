package com.parking.parkingtop.features.cliente.Perfil.presentation.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReservationProfileCard(
    reservation: Reservation,
    isCancelling: Boolean = false,
    onCancel: (() -> Unit)? = null,   // null = no mostrar botón (historial)
    modifier: Modifier = Modifier
) {
    var expanded        by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    // ── Diálogo de confirmación ───────────────────────────────────────────────
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title   = { Text("Cancelar reserva", fontWeight = FontWeight.Bold) },
            text    = { Text("¿Estás seguro que deseas cancelar tu reserva en ${reservation.parkingLotName}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showCancelDialog = false
                    onCancel?.invoke()
                }) {
                    Text("Cancelar reserva", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Volver", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    val statusColor = when (reservation.status) {
        "confirmed" -> Color(0xFF4CAF50)
        "active"    -> Color(0xFF2196F3)
        "pending"   -> Color(0xFFFF9800)
        "cancelled" -> Color(0xFFF44336)
        "completed" -> Color(0xFF9E9E9E)
        else        -> Color.Gray
    }

    val statusLabel = when (reservation.status) {
        "confirmed" -> "Confirmada"
        "active"    -> "Activa"
        "pending"   -> "Pendiente"
        "cancelled" -> "Cancelada"
        "completed" -> "Completada"
        else        -> reservation.status
    }

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Header siempre visible ────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.LocalParking,
                        null,
                        tint     = BlueSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        reservation.parkingLotName,
                        style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color    = TextPrimary,
                        maxLines = 1
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            statusLabel,
                            fontSize = 11.sp,
                            color    = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        null,
                        tint     = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Fecha rápida siempre visible
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                formatDateTime(reservation.startTime),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            // ── Detalle expandible ────────────────────────────────────────────
            AnimatedVisibility(
                visible = expanded,
                enter   = expandVertically(),
                exit    = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(label = "Inicio",   value = formatDateTime(reservation.startTime))
                    DetailRow(label = "Fin",      value = formatDateTime(reservation.endTime))
                    DetailRow(label = "Total",    value = "$${reservation.totalCost}")

                    // ── Botón cancelar — solo para reservas activas/pendientes/confirmadas
                    if (onCancel != null && reservation.status in listOf("pending", "confirmed", "active")) {
                        Spacer(modifier = Modifier.height(12.dp))
                        if (isCancelling) {
                            Row(
                                modifier          = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color       = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cancelando...", color = Color.Gray, fontSize = 13.sp)
                            }
                        } else {
                            OutlinedButton(
                                onClick  = { showCancelDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                )
                            ) {
                                Icon(
                                    Icons.Default.Cancel, null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cancelar reserva", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
    }
}

fun formatDateTime(dateTime: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        // Parsear como ZonedDateTime (detecta zona si viene en el string)
        val zonedDateTime = ZonedDateTime.parse(dateTime, inputFormatter)

        // Convertir a zona horaria de México
        val mexicoTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Mexico_City"))

        mexicoTime.format(outputFormatter)
    } catch (e: Exception) {
        dateTime
    }
}