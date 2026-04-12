package com.parking.parkingtop.features.subscritionplan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parking.parkingtop.ui.theme.TextPrimary

@Composable
fun FrequencySelector(
    selectedFrequency: String,
    onFrequencySelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = Color(0xFFF8F9FA),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Semanal", "Mensual", "Anual").forEach { freq ->
                val isSelected = selectedFrequency == freq
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { onFrequencySelected(freq) }
                        .then(if (isSelected) Modifier.border(0.5.dp, Color(0xFFEEEEEE), RoundedCornerShape(6.dp)) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = freq,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextPrimary else Color.Gray
                    )
                }
            }
        }
    }
}
