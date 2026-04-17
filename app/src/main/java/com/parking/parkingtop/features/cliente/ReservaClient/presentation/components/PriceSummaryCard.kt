package com.parking.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

/**
 * Card de resumen de precios
 */
@Composable
fun PriceSummaryCard(
    hours: Int,
    minutes: Int,
    baseCost: Double,
    additionalTime: Double,
    discounts: Double,
    total: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        val durationText = when {
            hours == 0 && minutes > 0  -> "${minutes}m"
            hours > 0 && minutes == 0  -> "${hours}h"
            hours > 0 && minutes > 0   -> "${hours}h ${minutes}m"
            else -> "—"
        }

        Text(
            text = "Resumen de Precios",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Precio base
        PriceRow(
            label = "Precio Base ($durationText)",
            amount = baseCost
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tiempo adicional
        PriceRow(
            label = "Tiempo Adicional",
            amount = additionalTime
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Descuentos
        PriceRow(
            label = "Descuentos",
            amount = -discounts,
            isDiscount = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = Color(0xFFEEEEEE))

        Spacer(modifier = Modifier.height(16.dp))

        // Total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )

            Text(
                text = "$${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = BlueSecondary
            )
        }
    }
}