package com.parking.parkingtop.features.cliente.Reviews.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.parking.parkingtop.features.cliente.Reviews.presentation.viewmodels.ReviewViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun ReviewScreen(
    parkingLotId: String,
    reservationId: String,
    parkingName: String,
    onSuccess: () -> Unit,
    onSkip: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var rating  by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier            = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Ícono de éxito ────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color(0xFFF0FDF4), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎉", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "¡Reserva completada!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "¿Cómo fue tu experiencia en\n$parkingName?",
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Estrellas ─────────────────────────────────────────────────
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..5).forEach { star ->
                        val isSelected = star <= rating
                        val scale by animateFloatAsState(
                            targetValue    = if (isSelected) 1.2f else 1f,
                            animationSpec  = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label          = "star_scale"
                        )
                        Icon(
                            imageVector        = if (isSelected) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "$star estrellas",
                            tint               = if (isSelected) Color(0xFFFFC107) else Color.LightGray,
                            modifier           = Modifier
                                .size(40.dp)
                                .scale(scale)
                                .clickable { rating = star }
                        )
                    }
                }

                // Label del rating
                if (rating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (rating) {
                            1    -> "Muy malo 😞"
                            2    -> "Malo 😕"
                            3    -> "Regular 😐"
                            4    -> "Bueno 😊"
                            else -> "¡Excelente! 🤩"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = BlueSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Comentario opcional ───────────────────────────────────────
                OutlinedTextField(
                    value         = comment,
                    onValueChange = { if (it.length <= 300) comment = it },
                    placeholder   = { Text("Cuéntanos más sobre tu experiencia (opcional)", color = Color.LightGray) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    minLines      = 3,
                    maxLines      = 4,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BlueSecondary,
                        unfocusedBorderColor = Color(0xFFEEEEEE)
                    ),
                    supportingText = {
                        Text(
                            "${comment.length}/300",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                )

                // ── Error ─────────────────────────────────────────────────────
                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text  = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Botones ───────────────────────────────────────────────────
                Button(
                    onClick  = {
                        viewModel.submitReview(
                            parkingLotId  = parkingLotId,
                            reservationId = reservationId,
                            rating        = rating,
                            comment       = comment.ifBlank { null }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape   = RoundedCornerShape(14.dp),
                    colors  = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                    enabled = rating > 0 && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                    } else {
                        Text(
                            "Enviar calificación",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick  = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Omitir por ahora", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}