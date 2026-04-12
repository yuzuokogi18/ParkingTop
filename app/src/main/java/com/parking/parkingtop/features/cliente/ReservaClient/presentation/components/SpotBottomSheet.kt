package com.parking.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotBottomSheet(
    spots: List<ParkingSpot>,
    selectedSpot: ParkingSpot?,
    onSpotSelected: (ParkingSpot?) -> Unit,  // null = sin preferencia
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor   = Color.White,
        shape            = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                "Seleccionar Espacio",
                style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color    = TextPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Los espacios grises no están disponibles",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Opción "Sin preferencia"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSpotSelected(null)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Sin preferencia (asignación automática)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                if (selectedSpot == null) {
                    Icon(Icons.Default.Check, null, tint = BlueSecondary, modifier = Modifier.size(18.dp))
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))

            // Grid de espacios
            LazyVerticalGrid(
                columns             = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.heightIn(max = 300.dp)
            ) {
                items(spots) { spot ->
                    val isSelected  = spot.id == selectedSpot?.id
                    val isAvailable = spot.isAvailable

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                color = when {
                                    isSelected  -> BlueSecondary
                                    isAvailable -> Color(0xFFF0F4FF)
                                    else        -> Color(0xFFEEEEEE)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = if (isSelected) 0.dp else 1.dp,
                                color = if (isAvailable) Color(0xFFCCCCCC) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = isAvailable) {
                                onSpotSelected(spot)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text      = spot.spotNumber,
                            fontSize  = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color     = when {
                                isSelected  -> Color.White
                                isAvailable -> BlueSecondary
                                else        -> Color.LightGray
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Leyenda
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                LegendItem(Color(0xFFF0F4FF), "Disponible")
                LegendItem(BlueSecondary,      "Seleccionado")
                LegendItem(Color(0xFFEEEEEE),  "No disponible")
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}