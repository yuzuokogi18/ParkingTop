package com.parking.parkingtop.features.cliente.Perfil.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun VehicleCard(
    name: String,
    plate: String,
    color: String,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}    // ✅ nuevo
) {
    // Estado local del diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ── Diálogo de confirmación ───────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Eliminar vehículo",
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
            },
            text = {
                Text(
                    "¿Estás seguro que deseas eliminar $name ($plate)? Esta acción no se puede deshacer.",
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Card(
        modifier  = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border    = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DirectionsCar,
                contentDescription = null,
                tint     = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name,  fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                Text(text = "Matrícula: $plate", fontSize = 13.sp, color = Color.Gray)
                Text(text = "Color: $color",     fontSize = 13.sp, color = Color.Gray)
            }

            // ── Editar ────────────────────────────────────────────────────────
            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier.size(18.dp),
                    tint     = Color.Gray
                )
            }

            // ── Eliminar ──────────────────────────────────────────────────────
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier.size(18.dp),
                    tint     = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}