package com.example.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.Money
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.parkingtop.features.cliente.ReservaClient.domain.entities.ParkingSpot
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodBottomSheet(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val methods = listOf(
        Triple("mercadopago", "MercadoPago", "Paga online de forma segura"),
        Triple("cash",        "Efectivo",    "Paga al llegar al estacionamiento")
    )

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
                "Método de Pago",
                style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color    = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            methods.forEach { (key, label, description) ->
                val isSelected = selectedMethod == key
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onMethodSelected(key)
                            onDismiss()
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (key == "mercadopago") Icons.Default.CreditCard
                            else Icons.Outlined.Money,
                            contentDescription = null,
                            tint     = if (isSelected) BlueSecondary else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = TextPrimary
                            )
                            Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                    if (isSelected) {
                        Icon(Icons.Default.Check, null, tint = BlueSecondary, modifier = Modifier.size(20.dp))
                    }
                }
                HorizontalDivider(color = Color(0xFFF5F5F5))
            }
        }
    }
}