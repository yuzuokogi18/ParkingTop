package com.example.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.Vehicle
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleBottomSheet(
    vehicles: List<Vehicle>,
    selectedVehicle: Vehicle?,
    onVehicleSelected: (Vehicle) -> Unit,
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
                "Seleccionar Vehículo",
                style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color    = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider(color = Color(0xFFEEEEEE))

            Spacer(modifier = Modifier.height(8.dp))

            if (vehicles.isEmpty()) {
                Box(
                    modifier         = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint     = Color.LightGray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No tienes vehículos registrados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            "Agrega uno desde tu perfil",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(vehicles) { vehicle ->
                        val isSelected = vehicle.id == selectedVehicle?.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onVehicleSelected(vehicle)
                                    onDismiss()
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier          = Modifier.weight(1f)
                            ) {
                                // Ícono de tipo de vehículo
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BlueSecondary.copy(alpha = 0.1f)
                                    else Color(0xFFF5F5F5)
                                ) {
                                    Icon(
                                        Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint     = if (isSelected) BlueSecondary else Color.Gray,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text  = "${vehicle.brand} ${vehicle.model}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold
                                                else FontWeight.Medium
                                            ),
                                            color = TextPrimary
                                        )
                                        // Badge "Por defecto"
                                        if (vehicle.isDefault) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = BlueSecondary.copy(alpha = 0.1f)
                                            ) {
                                                Text(
                                                    "Por defecto",
                                                    fontSize = 10.sp,
                                                    color    = BlueSecondary,
                                                    modifier = Modifier.padding(
                                                        horizontal = 6.dp, vertical = 2.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text  = "Matrícula: ${vehicle.licensePlate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Checkmark si está seleccionado
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Seleccionado",
                                    tint     = BlueSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        HorizontalDivider(
                            color     = Color(0xFFF5F5F5),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}