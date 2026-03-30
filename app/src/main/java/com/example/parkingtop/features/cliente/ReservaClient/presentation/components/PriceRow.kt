package com.example.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.parkingtop.ui.theme.TextPrimary

/**
 * Fila de precio individual
 */
@Composable
fun PriceRow(
    label: String,
    amount: Double,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = "${if (isDiscount && amount != 0.0) "-" else ""}$${"%.2f".format(if (isDiscount) -amount else amount)}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (isDiscount) Color(0xFF4CAF50) else TextPrimary
        )
    }
}