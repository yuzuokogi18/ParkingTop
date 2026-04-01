package com.example.parkingtop.features.cliente.BusquedaClient.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.cliente.BusquedaClient.presentation.viewmodels.FilterState
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilters: FilterState,
    onApply: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    // Estado local del sheet — solo se aplica al presionar el botón
    var maxPrice     by remember { mutableStateOf(currentFilters.maxPricePerHour ?: 200f) }
    var limitPrice   by remember { mutableStateOf(currentFilters.maxPricePerHour != null) }
    var onlyAvailable by remember { mutableStateOf(currentFilters.onlyAvailable) }
    var minRating    by remember { mutableStateOf(currentFilters.minRating) }

    ModalBottomSheet(
        onDismissRequest   = onDismiss,
        sheetState         = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor     = Color.White,
        shape              = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {

            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Filtros",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                TextButton(onClick = {
                    maxPrice      = 200f
                    limitPrice    = false
                    onlyAvailable = false
                    minRating     = 0f
                }) {
                    Text("Limpiar", color = BlueSecondary, fontSize = 13.sp)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            // ── Solo disponibles ──────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Solo disponibles",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Text(
                        "Excluir estacionamientos llenos",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked         = onlyAvailable,
                    onCheckedChange = { onlyAvailable = it },
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BlueSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Precio máximo ─────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Precio máximo por hora",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Switch(
                    checked         = limitPrice,
                    onCheckedChange = { limitPrice = it },
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BlueSecondary
                    )
                )
            }

            if (limitPrice) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("$10", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        "${"%.0f".format(maxPrice)} MXN/h",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold, color = BlueSecondary
                        )
                    )
                    Text("$200", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Slider(
                    value         = maxPrice,
                    onValueChange = { maxPrice = it },
                    valueRange    = 10f..200f,
                    steps         = 18,
                    colors        = SliderDefaults.colors(
                        thumbColor       = BlueSecondary,
                        activeTrackColor = BlueSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Rating mínimo ─────────────────────────────────────────────────
            Text(
                "Rating mínimo",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0f, 1f, 2f, 3f, 4f).forEach { value ->
                    val label = if (value == 0f) "Todos" else "${"%.0f".format(value)}★"
                    FilterChip(
                        selected = minRating == value,
                        onClick  = { minRating = value },
                        label    = { Text(label, fontSize = 12.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlueSecondary,
                            selectedLabelColor     = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Botón aplicar ─────────────────────────────────────────────────
            Button(
                onClick = {
                    onApply(
                        FilterState(
                            maxPricePerHour = if (limitPrice) maxPrice else null,
                            onlyAvailable   = onlyAvailable,
                            minRating       = minRating
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = BlueSecondary)
            ) {
                Text(
                    "Aplicar filtros",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}