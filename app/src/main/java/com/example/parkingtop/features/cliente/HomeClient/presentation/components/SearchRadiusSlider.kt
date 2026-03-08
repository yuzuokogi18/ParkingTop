package com.example.parkingtop.features.cliente.HomeClient.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@Composable
fun SearchRadiusSlider(
    position: Float,
    onPositionChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float> = 1f..20f
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Radio de búsqueda:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                "${position.toInt()} km",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Slider(
            value = position,
            onValueChange = onPositionChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = BlueSecondary,
                inactiveTrackColor = Color(0xFFEEEEEE)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
