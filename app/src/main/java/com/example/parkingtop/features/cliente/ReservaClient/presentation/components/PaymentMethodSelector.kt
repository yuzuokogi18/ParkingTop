package com.example.parkingtop.features.cliente.ReservaClient.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.parkingtop.ui.theme.TextPrimary

/**
 * Dropdown de método de pago
 */
@Composable
fun PaymentMethodSelector(
    selectedMethod: String,
    onMethodClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Método de Pago",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMethodClick() },
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar, // Cambiar por icono de tarjeta
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = selectedMethod,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = TextPrimary
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}