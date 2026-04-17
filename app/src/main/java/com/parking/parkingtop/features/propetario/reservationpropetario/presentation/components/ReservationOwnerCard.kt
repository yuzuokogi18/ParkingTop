package com.parking.parkingtop.features.propetario.reservationpropetario.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationOwner
import com.parking.parkingtop.features.propetario.reservationpropetario.domain.entities.ReservationStatus
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
private val isCash: (String?) -> Boolean = { it?.lowercase() == "cash" }

@Composable
fun ReservationOwnerCard(
    reservation: ReservationOwner,
    onConfirmCashPayment: () -> Unit,
    onCheckIn: () -> Unit,        // 🔥 NUEVO
    onCheckOut: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Header: cliente + precio ──────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsCar,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = reservation.clientName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${reservation.vehicleModel} · ${reservation.licensePlate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = "$${String.format("%.2f", reservation.price)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = BlueSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Horario ───────────────────────────────────────────────────
            Text(
                text = "${formatDateTime(reservation.startTime)} – ${formatDateTime(reservation.endTime)}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            reservation.checkInTime?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Entrada: ${formatDateTime(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            reservation.actualExitTime?.let {
                Text(
                    text = "Salida: ${formatDateTime(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // ── Chips: estado + método de pago ────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = reservation.status)
                PaymentMethodChip(paymentMethod = reservation.paymentMethod)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Acciones ──────────────────────────────────────────────────
            //  Solo se muestra el botón si:
            //   • el estado es CONFIRMED  (ya fue aceptada)
            //   • el método de pago es efectivo  (cash)
            //  Para pagos con tarjeta el cliente completa el pago por su cuenta.
            if (
                reservation.status == ReservationStatus.PENDING &&
                isCash(reservation.paymentMethod)
            ) {
                OutlinedButton(
                    onClick = onConfirmCashPayment,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BlueSecondary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueSecondary)
                ) {
                    Icon(
                        Icons.Outlined.Payments,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirmar pago en efectivo", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // 🚗 Check-in
            if (reservation.status == ReservationStatus.CONFIRMED) {
                Button(
                    onClick = onCheckIn,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
                ) {
                    Icon(Icons.Outlined.Login, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Check-in")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

// 🚪 Check-out
            if (reservation.status == ReservationStatus.ACTIVE) {
                Button(
                    onClick = onCheckOut,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Icon(Icons.Outlined.Logout, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Check-out")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Ver detalles ──────────────────────────────────────────────
            TextButton(
                onClick = onViewDetails,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Outlined.Visibility,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = BlueSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ver Detalles", color = BlueSecondary, fontSize = 12.sp)
            }
        }
    }
}

// ── Chips ─────────────────────────────────────────────────────────────────────

@Composable
fun StatusChip(status: ReservationStatus) {
    val (text, color, bgColor) = when (status) {
        ReservationStatus.PENDING   -> Triple("Pendiente",  Color(0xFFE65100), Color(0xFFFFF3E0))
        ReservationStatus.CONFIRMED -> Triple("Confirmada", Color(0xFF1B5E20), Color(0xFFE8F5E9))
        ReservationStatus.ACTIVE    -> Triple("En curso",   Color(0xFF1565C0), Color(0xFFE3F2FD)) // 🔥
        ReservationStatus.CANCELLED -> Triple("Cancelada",  Color(0xFFB71C1C), Color(0xFFFFEBEE))
        ReservationStatus.COMPLETED -> Triple("Completada", Color(0xFF0D47A1), Color(0xFFE3F2FD))
    }
    Surface(color = bgColor, shape = RoundedCornerShape(16.dp)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
fun PaymentMethodChip(paymentMethod: String?) {
    val (text, color, bgColor, icon) = when (paymentMethod?.lowercase()) {
        "cash"  -> Quadruple("Efectivo", Color(0xFF1B5E20), Color(0xFFE8F5E9),  Icons.Outlined.Payments)
        "card"  -> Quadruple("Tarjeta",  Color(0xFF0D47A1), Color(0xFFE3F2FD),  Icons.Outlined.CreditCard)
        else    -> return   // sin método definido → no mostramos chip
    }
    Surface(color = bgColor, shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(12.dp), tint = color)
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

// Helper para destructuring de 4 valores
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

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